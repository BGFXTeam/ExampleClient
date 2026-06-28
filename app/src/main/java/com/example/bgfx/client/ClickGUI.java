package com.example.bgfx.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;
import android.widget.TableRow.LayoutParams;

public class ClickGUI {

    private final Context ctx;
    private final WindowManager windowManager;
    private Typeface iconFont;

    private View floatingButton;
    private FrameLayout overlayRoot;

    private boolean isGuiOpen = false;
    private boolean isInitialized = false;

    public ClickGUI(Context context) {
        this.ctx = context;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        try {
            iconFont = Typeface.createFromAsset(ctx.getAssets(), "znfdev");
        } catch (Exception e) {
            iconFont = Typeface.DEFAULT;
        }

        showFloatingButton();
    }

    private int dpToPx(int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private GradientDrawable createRoundedBg(int color, int radiusDp) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(dpToPx(radiusDp));
        return gd;
    }

    private TextView createIconView(String iconName, int sizeDp, int color) {
        TextView tv = new TextView(ctx);
        tv.setTypeface(iconFont);
        tv.setText(iconName);
        tv.setTextColor(color);
        tv.setTextSize(sizeDp);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    // --- FLOATING BUTTON ---
    private void showFloatingButton() {
        TextView btn = new TextView(ctx);
        btn.setText("Client");
        btn.setTextColor(Color.WHITE);
        btn.setBackground(createRoundedBg(Color.parseColor("#1C1B1F"), 28));
        btn.setElevation(dpToPx(6));
        btn.setPadding(dpToPx(10), dpToPx(3), dpToPx(10), dpToPx(3));
        btn.setTextSize(11);
        btn.setTypeface(null, Typeface.BOLD);

        btn.setOnTouchListener(new View.OnTouchListener() {
                private int initialX, initialY;
                private float initialTouchX, initialTouchY;
                private boolean isMoved = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) floatingButton.getLayoutParams();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            isMoved = false;
                            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int deltaX = (int) (event.getRawX() - initialTouchX);
                            int deltaY = (int) (event.getRawY() - initialTouchY);
                            if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) isMoved = true;
                            params.x = initialX + deltaX;
                            params.y = initialY + deltaY;
                            windowManager.updateViewLayout(floatingButton, params);
                            break;
                        case MotionEvent.ACTION_UP:
                            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                            if (!isMoved) {
                                if (isGuiOpen) closeGUI(); else openGUI();
                            }
                            break;
                    }
                    return true;
                }
            });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 100;

        floatingButton = btn;
        windowManager.addView(floatingButton, params);
    }

    // --- BUILD GUI ONCE ---
    private void buildGUI() {
        overlayRoot = new FrameLayout(ctx);
        overlayRoot.setBackgroundColor(Color.argb(50, 0, 0, 0));
        overlayRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeGUI();
                }
            });

        int xOffset = dpToPx(10);
        ModuleCategory[] categories = ModuleCategory.values();

        for (int i = 0; i < categories.length; i++) {
            ModuleCategory cat = categories[i];
            CategoryPanel panel = new CategoryPanel(ctx, cat);

            FrameLayout.LayoutParams panelParams = new FrameLayout.LayoutParams(dpToPx(90), FrameLayout.LayoutParams.WRAP_CONTENT);
            panel.setLayoutParams(panelParams);

            // Align horizontally side-by-side at the top
            panel.setX(xOffset);
            panel.setY(dpToPx(10));
            overlayRoot.addView(panel);

            xOffset += dpToPx(95);
        }

        // --- CLOSE BUTTON ---
        TextView closeBtn = createIconView("close", 14, Color.WHITE);
        closeBtn.setElevation(dpToPx(6));
        closeBtn.setBackground(createRoundedBg(Color.parseColor("#1C1B1F"), 16));
        closeBtn.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        closeParams.gravity = Gravity.TOP | Gravity.END;
        closeParams.setMargins(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeGUI();
                }
            });
        overlayRoot.addView(closeBtn, closeParams);

        isInitialized = true;
    }

    private void openGUI() {
        if (!isInitialized) buildGUI();

        isGuiOpen = true;

        // CRITICAL FIX: Cancel any ongoing animations and remove stale listeners
        // This prevents a delayed onClose animation from removing the view after we reopen it
        overlayRoot.animate().cancel();
        overlayRoot.animate().setListener(null);

        if (overlayRoot.isAttachedToWindow()) {
            // The view is already attached (probably still fading out from a quick close).
            // Just reverse the animation back to visible instead of re-adding it.
            overlayRoot.animate().alpha(1f).setDuration(200).start();
        } else {
            // The view is fully detached, reset its state and add it to the WindowManager
            overlayRoot.setAlpha(0f);

            WindowManager.LayoutParams overlayParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            );

            windowManager.addView(overlayRoot, overlayParams);

            // Animate in
            overlayRoot.animate().alpha(1f).setDuration(200).start();
        }
    }

    private void closeGUI() {
        isGuiOpen = false;

        if (overlayRoot != null && overlayRoot.isAttachedToWindow()) {
            // CRITICAL FIX: Cancel ongoing animations and clear listeners 
            // in case it's currently fading in and we want to reverse it immediately
            overlayRoot.animate().cancel();
            overlayRoot.animate().setListener(null);

            overlayRoot.animate()
                .alpha(0f)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Double-check that we still want it closed 
                        // (Prevents a race condition if openGUI() was called during the fade-out)
                        if (overlayRoot != null && overlayRoot.isAttachedToWindow() && !isGuiOpen) {
                            windowManager.removeView(overlayRoot);
                        }
                        // Clean up the listener to prevent memory leaks and stale calls
                        overlayRoot.animate().setListener(null);
                    }
                })
                .start();
        }
    }

    // --- CUSTOM VIEW: FLOATING CATEGORY PANEL ---
    private class CategoryPanel extends LinearLayout {
        private boolean isOpen = false;
        private LinearLayout moduleListContainer;

        public CategoryPanel(Context context, final ModuleCategory cat) {
            super(context);
            setOrientation(VERTICAL);
            setBackground(createRoundedBg(Color.parseColor("#1C1B1F"), 6));
            setElevation(dpToPx(10));
            setClipToOutline(true);

            setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });

            // --- HEADER ---
            LinearLayout header = new LinearLayout(context);
            header.setOrientation(HORIZONTAL);
            header.setGravity(Gravity.CENTER_VERTICAL);
            header.setPadding(dpToPx(6), dpToPx(4), dpToPx(6), dpToPx(4));
            header.setBackground(createRoundedBg(Color.parseColor("#2D2D30"), 6));

            String iconName = "category";
            if (cat == ModuleCategory.COMBAT) iconName = "swords";
            if (cat == ModuleCategory.MOVEMENT) iconName = "directions_run";
            if (cat == ModuleCategory.VISUAL) iconName = "visibility";
            if (cat == ModuleCategory.WORLD) iconName = "public";
            if (cat == ModuleCategory.MISC) iconName = "build";

            header.addView(createIconView(iconName, 10, Color.parseColor("#D0BCFF")));

            TextView title = new TextView(context);
            title.setText(cat.getName());
            title.setTextColor(Color.WHITE);
            title.setTextSize(9);
            title.setTypeface(null, Typeface.BOLD);
            title.setPadding(dpToPx(3), 0, 0, 0);
            LayoutParams titleParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
            header.addView(title, titleParams);

            final TextView expandIcon = createIconView("expand_more", 10, Color.WHITE);
            header.addView(expandIcon);

            addView(header);

            // --- MODULE LIST ---
            final ScrollView moduleScroll = new ScrollView(context);
            moduleScroll.setVerticalScrollBarEnabled(false);
            moduleListContainer = new LinearLayout(context);
            moduleListContainer.setOrientation(VERTICAL);
            moduleListContainer.setPadding(dpToPx(2), dpToPx(1), dpToPx(2), dpToPx(2));
            moduleScroll.addView(moduleListContainer);

            LayoutParams listParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            moduleScroll.setLayoutParams(listParams);
            moduleScroll.setVisibility(GONE);

            addView(moduleScroll);

            populateModules(cat);

            header.setOnTouchListener(new OnTouchListener() {
                    private float startX, startY, startTransX, startTransY;
                    private boolean isDragging;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                startX = event.getRawX();
                                startY = event.getRawY();
                                startTransX = CategoryPanel.this.getX();
                                startTransY = CategoryPanel.this.getY();
                                isDragging = false;
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                float dx = event.getRawX() - startX;
                                float dy = event.getRawY() - startY;
                                if (Math.abs(dx) > 5 || Math.abs(dy) > 5) isDragging = true;
                                if (isDragging) {
                                    CategoryPanel.this.setX(startTransX + dx);
                                    CategoryPanel.this.setY(startTransY + dy);
                                }
                                return true;
                            case MotionEvent.ACTION_UP:
                                if (!isDragging) {
                                    isOpen = !isOpen;
                                    expandIcon.setText(isOpen ? "expand_less" : "expand_more");
                                    expandIcon.animate().rotation(isOpen ? 180 : 0).setDuration(200).start();
                                    animateViewHeight(moduleScroll, isOpen);
                                }
                                return false;
                        }
                        return false;
                    }
                });
        }

        private void populateModules(ModuleCategory cat) {
            List<Module> modules = ModuleManager.INSTANCE.getModules();
            for (int i = 0; i < modules.size(); i++) {
                final Module mod = modules.get(i);
                if (mod.getCategory() != cat) continue;
                moduleListContainer.addView(new ModuleItemView(ctx, mod, iconFont));
            }
        }
    }

    // --- CUSTOM VIEW: MODULE ITEM ---
    private class ModuleItemView extends LinearLayout {
        public ModuleItemView(Context context, final Module mod, final Typeface iFont) {
            super(context);
            setOrientation(VERTICAL);
            setPadding(0, dpToPx(1), 0, dpToPx(1));

            final LinearLayout topRow = new LinearLayout(context);
            topRow.setOrientation(HORIZONTAL);
            topRow.setGravity(Gravity.CENTER_VERTICAL);
            topRow.setPadding(dpToPx(4), dpToPx(2), dpToPx(4), dpToPx(2));
            topRow.setBackground(createRoundedBg(Color.argb(26, 255, 255, 255), 3));

            final TextView name = new TextView(context);
            name.setText(mod.getName());
            name.setTextColor(mod.isEnabled() ? Color.parseColor("#D0BCFF") : Color.parseColor("#CCCCCC"));
            name.setTextSize(8);
            name.setTypeface(null, Typeface.BOLD);

            LayoutParams nameParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
            topRow.addView(name, nameParams);

            final TextView expandIcon = createIconView("expand_more", 8, Color.parseColor("#AAAAAA"));
            if (!mod.getSettings().isEmpty()) {
                topRow.addView(expandIcon);
            }

            final LinearLayout settingsContainer = new LinearLayout(context);
            settingsContainer.setOrientation(VERTICAL);
            settingsContainer.setPadding(dpToPx(6), 0, 0, 0);
            settingsContainer.setVisibility(GONE);

            for (final Setting s : mod.getSettings()) {
                if (s instanceof Setting.BooleanSetting) {
                    settingsContainer.addView(new BooleanSettingView(context, (Setting.BooleanSetting) s, iFont));
                } else if (s instanceof Setting.FloatSetting) {
                    settingsContainer.addView(new SliderSettingView(context, (Setting.FloatSetting) s, iFont));
                }
            }

            // Toggle Module
            name.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mod.toggle();
                        name.setTextColor(mod.isEnabled() ? Color.parseColor("#D0BCFF") : Color.parseColor("#CCCCCC"));
                    }
                });

            // Expand Settings
            if (!mod.getSettings().isEmpty()) {
                topRow.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean expanded = settingsContainer.getVisibility() == VISIBLE;
                            settingsContainer.setVisibility(expanded ? GONE : VISIBLE);
                            expandIcon.setText(expanded ? "expand_more" : "expand_less");
                            expandIcon.animate().rotation(expanded ? 0 : 180).setDuration(150).start();
                            animateViewHeight(settingsContainer, !expanded);
                        }
                    });
            }

            addView(topRow);
            addView(settingsContainer);
        }
    }

    // --- CUSTOM VIEW: BOOLEAN SETTING ---
    private class BooleanSettingView extends LinearLayout {
        public BooleanSettingView(Context context, final Setting.BooleanSetting setting, Typeface iFont) {
            super(context);
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);
            setPadding(0, dpToPx(2), 0, dpToPx(2));

            TextView name = new TextView(context);
            name.setText(setting.getName());
            name.setTextColor(Color.argb(179, 255, 255, 255));
            name.setTextSize(7);

            final TextView checkIcon = createIconView("check_box_outline_blank", 9, Color.argb(179, 255, 255, 255));
            if (setting.getValue()) {
                checkIcon.setText("check_box");
                checkIcon.setTextColor(Color.parseColor("#D0BCFF"));
            }

            LayoutParams nameParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
            addView(name, nameParams);
            addView(checkIcon);

            setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setting.setValue(!setting.getValue());
                        checkIcon.setText(setting.getValue() ? "check_box" : "check_box_outline_blank");
                        checkIcon.setTextColor(setting.getValue() ? Color.parseColor("#D0BCFF") : Color.argb(179, 255, 255, 255));
                    }
                });
        }
    }

    // --- CUSTOM VIEW: SLIDER SETTING ---
    private class SliderSettingView extends LinearLayout {
        public SliderSettingView(Context context, final Setting.FloatSetting setting, Typeface iFont) {
            super(context);
            setOrientation(VERTICAL);
            setPadding(0, dpToPx(2), dpToPx(4), 0);

            LinearLayout nameRow = new LinearLayout(context);
            nameRow.setOrientation(HORIZONTAL);

            TextView name = new TextView(context);
            name.setText(setting.getName());
            name.setTextColor(Color.argb(179, 255, 255, 255));
            name.setTextSize(7);

            final TextView valueText = new TextView(context);
            valueText.setText(String.format("%.1f", setting.getValue()));
            valueText.setTextColor(Color.parseColor("#D0BCFF"));
            valueText.setTextSize(7);

            LayoutParams nameParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
            nameRow.addView(name, nameParams);
            nameRow.addView(valueText);
            addView(nameRow);

            final SliderTrackView track = new SliderTrackView(context, setting, valueText);
            track.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(10)));
            addView(track);
        }

        private class SliderTrackView extends View {
            private Setting.FloatSetting setting;
            private TextView valueText;
            private Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            private Paint activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            private Paint thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            public SliderTrackView(Context context, Setting.FloatSetting setting, TextView valueText) {
                super(context);
                this.setting = setting;
                this.valueText = valueText;
                trackPaint.setColor(Color.argb(51, 255, 255, 255));
                activePaint.setColor(Color.parseColor("#D0BCFF"));
                thumbPaint.setColor(Color.parseColor("#D0BCFF"));
            }

            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                int h = getHeight();
                int w = getWidth();
                float cy = h / 2f;
                float trackRadius = dpToPx(1);

                canvas.drawRoundRect(0, cy - trackRadius, w, cy + trackRadius, trackRadius, trackRadius, trackPaint);

                float percent = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
                float activeW = w * percent;
                canvas.drawRoundRect(0, cy - trackRadius, activeW, cy + trackRadius, trackRadius, trackRadius, activePaint);

                thumbPaint.clearShadowLayer();
                thumbPaint.setShadowLayer(dpToPx(2), 0, dpToPx(1), Color.argb(85, 208, 188, 255));
                canvas.drawCircle(activeW, cy, dpToPx(3), thumbPaint);
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float w = getWidth();
                    float percent = Math.max(0, Math.min(1, x / w));
                    float val = setting.getMin() + percent * (setting.getMax() - setting.getMin());
                    setting.setValue(val);
                    valueText.setText(String.format("%.1f", val));
                    invalidate();
                    return true;
                }
                return super.onTouchEvent(event);
            }
        }
    }

    // --- HELPER: Smooth Height Animation ---
    private void animateViewHeight(final View v, final boolean expand) {
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        final int targetHeight = v.getMeasuredHeight();

        if (expand) {
            v.getLayoutParams().height = 0;
            v.setVisibility(View.VISIBLE);
            ValueAnimator anim = ValueAnimator.ofInt(0, targetHeight);
            anim.setDuration(150);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                        v.requestLayout();
                    }
                });
            // Critical Fix: Reset height to WRAP_CONTENT when done so children can expand it later
            anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                    }
                });
            anim.start();
        } else {
            ValueAnimator anim = ValueAnimator.ofInt(v.getHeight(), 0);
            anim.setDuration(150);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                        v.requestLayout();
                    }
                });
            anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                    }
                });
            anim.start();
        }
    }
}
