package com.sinyuk.yuk.utils;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.widget.TextView;

import in.uncod.android.bypass.style.TouchableUrlSpan;


/**
 * Utility methods for working with HTML.
 */
public class HtmlUtils {

    private HtmlUtils() { }

    /**
     * Work around some 'features' of TextView and URLSpans. i.e. vanilla URLSpans do not react to
     * touch so we replace them with our own {@link io.plaidapp.ui.span
     * .TouchableUrlSpan}
     * & {@link io.plaidapp.util.LinkTouchMovementMethod} to fix this.
     * <p/>
     * Setting a custom MovementMethod on a TextView also alters touch handling (see
     * TextView#fixFocusableAndClickableSettings) so we need to correct this.
     *
     * @param textView
     * @param input
     */
    public static void setTextWithNiceLinks(TextView textView, CharSequence input) {
        textView.setText(input);
        textView.setMovementMethod(LinkTouchMovementMethod.getInstance());
        textView.setFocusable(false);
        textView.setClickable(false);
        textView.setLongClickable(false);
    }

    /**
     * Parse the given input using {@link TouchableUrlSpan}s
     * rather than vanilla {@link android.text.style.URLSpan}s so that they respond to touch.
     *
     * @param input
     * @param linkTextColor
     * @param linkHighlightColor
     * @return
     */
    public static SpannableStringBuilder parseHtml(String input,
                                    ColorStateList linkTextColor,
                                    @ColorInt int linkHighlightColor) {
        SpannableStringBuilder spanned = (SpannableStringBuilder) Html.fromHtml(input);

        // strip any trailing newlines
        while (spanned.charAt(spanned.length() - 1) == '\n') {
            spanned = spanned.delete(spanned.length() - 1, spanned.length());
        }

        URLSpan[] urlSpans = spanned.getSpans(0, spanned.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            int start = spanned.getSpanStart(urlSpan);
            int end = spanned.getSpanEnd(urlSpan);
            spanned.removeSpan(urlSpan);
            spanned.setSpan(new TouchableUrlSpan(urlSpan.getURL(), linkTextColor,
                    linkHighlightColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanned;
    }

    public static void parseAndSetText(TextView textView, String input) {
        if (TextUtils.isEmpty(input)) return;
        setTextWithNiceLinks(textView, parseHtml(input, textView.getLinkTextColors(),
                textView.getHighlightColor()));
    }

}
