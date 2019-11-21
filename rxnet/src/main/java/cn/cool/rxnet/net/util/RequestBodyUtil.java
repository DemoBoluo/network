package cn.cool.rxnet.net.util;

import java.io.File;
import java.util.List;

import cn.cool.rxnet.net.body.UploadFileRequestBody;
import cn.cool.rxnet.net.callback.Rx2ActivityObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 *@author boluo
 *类描述：
 *版本号：
 *修改时间： 2019/6/10
 */
public class RequestBodyUtil {
    private final static String TAG = "RequestBodyUtil";
    //提交json
    public static RequestBody getBodyJson(Object object){
        String objectJson = GsonUtil.parseBeanToJson(object);
        Lutils.i(TAG,"getBodyJson param pareToJson:"+objectJson);
        ObjectHelper.requireNonNull(objectJson,"when getBodyJson, parseToJson return null");
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), objectJson);
    }

    //获得FileRequestBody
    public static RequestBody getFileBody(File file){
        ObjectHelper.requireNonNull(file,"getFileBody param file not null");
        return RequestBody.create(MediaType.parse("application/octet-stream"), file);
    }

    /**
     * 单文件上传构造.
     *
     * @param file 文件
     * @return MultipartBody
     */
    public static MultipartBody fileToMultipartBody(File file, String json,RequestBody requestBody) {

        ObjectHelper.requireNonNull(json,"fileToMultipartBody param json not null");
        Lutils.i(TAG,"===fileToMultipartBody param json=="+json);
        MultipartBody.Builder builder = new MultipartBody.Builder();

//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("fileName", file.getName());
//        jsonObject.addProperty("fileSha", Utils.getFileSha1(file));
//        jsonObject.addProperty("appId", "test0002");

        builder.addFormDataPart("file", file.getName(), requestBody);
        //builder.addFormDataPart("params", json);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }


    /**
     * 多文件上传构造.
     *
     * @param files 文件列表
     * @param activityObserver 文件上传回调
     * @return MultipartBody
     */
    public static MultipartBody filesToMultipartBody(String jsonArray, List<File> files,
                                                     Rx2ActivityObserver<ResponseBody> activityObserver) {
        ObjectHelper.requireNonNull(jsonArray,"filesToMultipartBody param jsonArray not null");
        ObjectHelper.requireNonNull(files,"filesToMultipartBody param files not null");
        ObjectHelper.requireNonNull(files,"filesToMultipartBody param activityObserver not null");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (File file : files) {
            UploadFileRequestBody uploadFileRequestBody =
                    new UploadFileRequestBody(file, activityObserver);

//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("fileName", file.getName());
//            jsonObject.addProperty("fileSha", Utils.getFileSha1(file));
//            jsonObject.addProperty("appId", "test0002");
//            jsonArray.add(jsonObject);

            builder.addFormDataPart("file", file.getName(), uploadFileRequestBody);
        }
        //builder.addFormDataPart("params", jsonArray);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

}
