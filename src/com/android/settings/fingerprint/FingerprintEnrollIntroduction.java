/*
 * Copyright (C) 2015 The Android Open Source Project
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
 * limitations under the License
 */

package com.android.settings.fingerprint;

import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;

import com.android.settings.ChooseLockSettingsHelper;
import com.android.settings.HelpUtils;
import com.android.settings.R;

/**
 * Onboarding activity for fingerprint enrollment.
 */
public class FingerprintEnrollIntroduction extends FingerprintEnrollBase {

    private boolean mHasPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_enroll_introduction);
        setHeaderText(R.string.security_settings_fingerprint_enroll_introduction_title);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.learn_more_button).setOnClickListener(this);
        final int passwordQuality = new ChooseLockSettingsHelper(this).utils()
                .getActivePasswordQuality(UserHandle.myUserId());
        mHasPassword = passwordQuality != DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED;
    }

    @Override
    protected void onNextButtonClick() {
        Intent intent = new Intent();
        final String clazz;
        if (!mHasPassword) {
            // No fingerprints registered, launch into enrollment wizard.
            clazz = FingerprintEnrollOnboard.class.getName();
        } else {
            // Lock thingy is already set up, launch directly into find sensor step from wizard.
            clazz = FingerprintEnrollFindSensor.class.getName();
        }
        intent.setClassName("com.android.settings", clazz);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_FINISHED) {
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            finish();
        }
        if (v.getId() == R.id.learn_more_button) {
            launchFingerprintHelp();
        }
        super.onClick(v);
    }

    private void launchFingerprintHelp() {
        Intent helpIntent = HelpUtils.getHelpIntent(this,
                getString(R.string.help_url_fingerprint), getClass().getName());
        startActivity(helpIntent);
    }
}
