/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lzh.framework.updatepluginlib.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import org.lzh.framework.updatepluginlib.R;
import org.lzh.framework.updatepluginlib.base.CheckNotifier;
import org.lzh.framework.updatepluginlib.util.ActivityManager;
import org.lzh.framework.updatepluginlib.util.SafeDialogHandle;

/**
 * 默认使用的在检查到有更新时的通知创建器：创建一个弹窗提示用户当前有新版本需要更新。
 *
 * @author haoge
 * @see CheckNotifier
 */
public class DefaultUpdateNotifier extends CheckNotifier {
    @Override
    public Dialog create(Activity activity) {
        String updateContent = ActivityManager.get().topActivity().getString(R.string.hsc_update_version_number)+ update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        AlertDialog.Builder builder =  new AlertDialog.Builder(activity)
                .setMessage(updateContent)
                .setTitle(ActivityManager.get().topActivity().getString(R.string.hsc_update_you_have_a_new_version_to_update))
                .setPositiveButton(ActivityManager.get().topActivity().getString(R.string.hsc_update_update_now), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendDownloadRequest();
                        SafeDialogHandle.safeDismissDialog((Dialog) dialog);
                    }
                });
        if (update.isIgnore() && !update.isForced()) {
            builder.setNeutralButton(ActivityManager.get().topActivity().getString(R.string.hsc_update_ignore_this_version), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendUserIgnore();
                    SafeDialogHandle.safeDismissDialog((Dialog) dialog);
                }
            });
        }

        if (!update.isForced()) {
            builder.setNegativeButton(ActivityManager.get().topActivity().getString(R.string.hsc_update_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendUserCancel();
                    SafeDialogHandle.safeDismissDialog((Dialog) dialog);
                }
            });
        }
        builder.setCancelable(false);
        return builder.create();
    }
}
