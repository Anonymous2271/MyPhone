package com.example.leosunzh.myphone.logic;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理系统联系人数据逻辑操作类
 * 以姓名为唯一标识
 * Created by leosunzh on 2015/12/14.
 */
public class MyResolver {
    Context context;
    //getContentResolver()方法需要Context对象调用，传入Context对象
    public MyResolver(Context context) {
        this.context = context;
    }

    /**
     * 查询联系人
     * @param selectionArgs 查询约束条件，条件和参数一起
     * @return
     */
    public List<Person> readContacts(String selectionArgs) {
        List<Person> list = new ArrayList<Person>();
        String projection[] = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, selectionArgs, null, "sort_key");
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                Bitmap photo = getHighPhoto(id);//在此读取头像会使程序运行的特别慢，所以读取头像都传入联系人Id个别读取
                list.add(new Person(id, number, name, Trans2PinYin.getInstance().convertAll(name).toUpperCase()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) ;
                cursor.close();
                return list;
        }
    }

    /**
     * 添加联系人
     * @param number 待添加的联系人号码
     * @param name 待添加的联系人姓名
     */
    public void addContact(String number,String name){
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();
        //向data表插入姓名
        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);//添加id
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);//添加内容类型（MIMETYPE）
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);//添加名字，添加到first name位置
        context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI,values);
        //向data表插入号码
        values.clear();
        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
    }

    /**
     * 修改联系人
     * @param name 待修改的联系人姓名
     * @param newName 新的姓名
     * @param newNumber 新的号码
     */
    public void upDateContact(String name,String newNumber,String newName){
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.RawContacts.Data._ID},"display_name=?", new String[]{name}, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,newNumber);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values,
                    ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                    new String[]{String.valueOf(id), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE});
            values.clear();
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,newName);
            context.getContentResolver().update(ContactsContract.Data.CONTENT_URI,values,
                    ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                    new String[]{String.valueOf(id),  ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE});
        }
        cursor.close();
    }

    /**
     * 删除联系人
     * @param name 待删除的联系人姓名
     */
    public void deleteContact(String name){
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.RawContacts.Data._ID},"display_name=?", new String[]{name}, null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            context.getContentResolver().delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            context.getContentResolver().delete(uri, "raw_contact_id=?", new String[]{id+""});
        }
        cursor.close();
    }

    /**
     * 获取通话记录
     */
    public List<CallLog> readCallLog(String who){
        String[] projection = {android.provider.CallLog.Calls._ID,android.provider.CallLog.Calls.NUMBER,android.provider.CallLog.Calls.CACHED_NAME,
                android.provider.CallLog.Calls.TYPE,android.provider.CallLog.Calls.DATE,android.provider.CallLog.Calls.DURATION};
//
        List<CallLog> list = new ArrayList<CallLog>();
        Cursor cursor = context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,projection,who,null,android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);
        if(cursor.moveToFirst()){
            do {
                //id
                String id = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls._ID));
                //号码
                String number = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                //呼叫类型
                String type;
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE)))) {
                    case android.provider.CallLog.Calls.INCOMING_TYPE:
                        type = "呼入";
                        break;
                    case android.provider.CallLog.Calls.OUTGOING_TYPE:
                        type = "呼出";
                        break;
                    case android.provider.CallLog.Calls.MISSED_TYPE:
                        type = "未接";
                        break;
                    default:
                        type = "挂断";//应该是挂断.根据我手机类型判断出的
                        break;
                }
                SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE))));
                //呼叫时间
                String time = sfd.format(date);
                //联系人
                String name;
                if (cursor.getString(cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME)) == null) {
                    name = "陌生人";
                } else{
                    name = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME));
                }
                //通话时间,单位:s
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.DURATION));

                list.add(new CallLog(id,name,number,type,time,duration));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 删除通话记录
     */
    public void deleteCallLog(String id){
        context.getContentResolver().delete(android.provider.CallLog.Calls.CONTENT_URI,
                android.provider.CallLog.Calls._ID+"=?",new String[]{id});
    }

    /**
     * 获取联系人头像
     * @param people_id 联系人ID
     * @return 联系人的头像
     */
    public  Bitmap getHighPhoto(String people_id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(people_id));
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri, true);
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }

    /**
     * 添加或修改头像
     * @param id
     * @param photo
     */
    public void updateOrInsertPhoto(String id, byte[] photo) {
//        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
//        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.RawContacts.Data._ID},"display_name=?", new String[]{name}, null);
//        if(cursor.moveToFirst()) {

//            int rawContactId = cursor.getInt(0);
            int rawContactId = Integer.parseInt(id);
            if (getHighPhoto(String.valueOf(rawContactId)) != null) {
                ContentValues values = new ContentValues();
                values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, photo);

                String selection = ContactsContract.Contacts.Data.RAW_CONTACT_ID + "=? and " + ContactsContract.Contacts.Data.MIMETYPE
                        + "=?";
                String[] selectionArgs = new String[]{
                        String.valueOf(rawContactId), ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE};

                context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, selection,
                        selectionArgs);
            } else {
                ContentValues values = new ContentValues();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, photo);

                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            }
//        }
//        cursor.close();
    }
//
//    public void updateOrInsertPhoto(String id, byte[] photo) {
//        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
//        if (getHighPhoto(id) == null) {
//            Toast.makeText(context,"11111",Toast.LENGTH_SHORT).show();
//            ContentValues values = new ContentValues();
//            values.put("raw_contact_id",id);
//            values.put("data15",photo);
//            values.put("mimetype", "vnd.android.cursor.item/photo");
//            context.getContentResolver().insert(uri, values);
//        }
//    }

    /**
     * 工具类
     * @param bm
     * @return
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

//    /**
//     * 得到名字拼音大写字母
//     * @param input
//     * @return
//     */
//    public  String getPinYin(String input) {
//        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(input);
//        StringBuilder sb = new StringBuilder();
//        if (tokens != null && tokens.size() > 0) {
//            for (HanziToPinyin.Token token : tokens) {
//                if (token.type == HanziToPinyin.Token.PINYIN) {
//                    sb.append(token.target);
//                } else {
//                    sb.append(token.source);
//                }
//            }
//        }
//        return sb.toString();
//    }

}
