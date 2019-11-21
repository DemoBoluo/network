package cn.cool.rxnet.net.body;


import java.io.File;
import java.io.IOException;

import cn.cool.rxnet.net.callback.Rx2ActivityObserver;
import cn.cool.rxnet.net.util.RequestBodyUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 *@author boluo
 *classdescrption：上传文件RequestBody
 *version：1.0.0
 *date： 2019/11/21
 */
public class UploadFileRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    private Rx2ActivityObserver<ResponseBody> fileUploadObserver;

    public UploadFileRequestBody(File file, Rx2ActivityObserver<ResponseBody> fileUploadObserver) {
        this.mRequestBody = RequestBodyUtil.getFileBody(file);
        this.fileUploadObserver = fileUploadObserver;
    }


    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        // 写入
        mRequestBody.writeTo(bufferedSink);
        // 刷新
        // 必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();

    }

    /**
     * CountingSink.
     */
    final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            bytesWritten += byteCount;
            if (fileUploadObserver != null) {
                fileUploadObserver.onProgress(bytesWritten, contentLength());
            }
        }

    }

}
