package example.diary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.diary.R;



public class RichEditTextView extends EditText {

    private Context mContext;
    private Editable mEditable;

    public RichEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public RichEditTextView(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 添加一个图片
     *
     * @param bitmap
     * @param filePath
     */
    public void addImage(Bitmap bitmap, String filePath) {
        Log.i("imgpath", filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        Bitmap newBitmap = zoomImage(bitmap);
        ImageSpan imgSpan = new ImageSpan(mContext, newBitmap);
        spanString.setSpan(imgSpan, 0, pathTag.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mEditable == null) {
            mEditable = this.getText(); // 获取edittext内容
        }
        int start = this.getSelectionStart(); // 设置欲添加的位置
        mEditable.insert(start, spanString); // 设置spanString要添加的位置
        this.setText(mEditable);
        this.setSelection(start, spanString.length());
    }

    /**
     * @param bitmap
     * @param filePath
     * @param start
     * @param end
     */
    public void addImage(Bitmap bitmap, String filePath, int start, int end) {
        Log.i("imgpath", filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        Bitmap newBitmap = zoomImage(bitmap);
        ImageSpan imgSpan = new ImageSpan(mContext, newBitmap);
        spanString.setSpan(imgSpan, 0, pathTag.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable editable = this.getText(); // 获取edittext内容
        editable.delete(start, end);//删除
        editable.insert(start, spanString); // 设置spanString要添加的位置
    }

    /**
     * @param filePath
     * @param start
     * @param end
     */
    public void addDefaultImage(String filePath, int start, int end) {
        Log.i("imgpath", filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        // 获取屏幕的宽高
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_default);
        Bitmap newBitmap = zoomImage(bitmap);
        ImageSpan imgSpan = new ImageSpan(mContext, newBitmap);
        spanString.setSpan(imgSpan, 0, pathTag.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mEditable == null) {
            mEditable = this.getText(); // 获取edittext内容
        }
        mEditable.delete(start, end);//删除
        mEditable.insert(start, spanString); // 设置spanString要添加的位置
    }

    /**
     * 对图片进行缩放
     *
     * @param bgimage
     * @return
     */
    private Bitmap zoomImage(Bitmap bgimage) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int bmWidth = bgimage.getWidth();
        int bmHeight = bgimage.getHeight();
        int zoomWidth = getWidth() - (paddingLeft + paddingRight);
        int zoomHeight = (int) (((float) zoomWidth / (float) bmWidth) * bmHeight);

        int newHeight = bmHeight > 600 ? 600 : bmHeight;
        int newWidth = newHeight * bmWidth / bmHeight;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / bmWidth;
        float scaleHeight = ((float) newHeight) / bmHeight;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) bmWidth,
                (int) bmHeight, matrix, true);
        return bitmap;
    }

    public void setRichText(String text) {
        this.setText("");
        this.mEditable = this.getText();
        this.mEditable.append(text);
        //遍历查找
        String str = "<img src=\"([/\\w\\W/\\/.]*)\"\\s*/>";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            final String localFilePath = matcher.group(1);
            String matchString = matcher.group();
            final int matchStringStartIndex = text.indexOf(matchString);
            final int matchStringEndIndex = matchStringStartIndex + matchString.length();
            Glide.with(mContext).load(localFilePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    addImage(resource, localFilePath, matchStringStartIndex, matchStringEndIndex);
                }
            });
        }
        this.setText(mEditable);
    }

    public String getRichText() {
        return this.getText().toString();
    }
}
