# CasHUB Log Tester (Demo App)

This application is a specialized tool designed to verify the **Log Upload** features of the **CasHUB platform**, specifically supporting Manual Upload and Active Upload via Intent.

---

## 1. Project Configuration
To ensure compatibility with the CasHUB portal, the following parameters are used:

* **Folder Path:** `/mnt/sdcard/cashub_demo`
* **File Name:** `demo_logs.csv`
* **Package Name:** `com.example.cashub_logs_demo`
* **Target SDK:** 34 (Android 14)
* **Minimum SDK:** 26 (Android 8.0)

---

## 2. Test Scenarios

### Scenario A: Manual Log Upload (Chapter 7)
This scenario tests the portal's ability to "pull" a file from a known path on the device.

1. **Generate Log:** Open the app and click **"1. Save Logcat to SD Card"**.
2. **Verify:** Ensure the UI status displays `File Saved Success`.
3. **CasHUB Portal Setup:**
    * Navigate to **Terminal Details** > **Upload** > **CREATE**.
    * **Name:** `demo_logs.csv`
    * **File Path:** `/mnt/sdcard/cashub_demo`
4. **Execute:** Click **CREATE**.
5. **Result:** Monitor the status until it transitions to **"Uploaded"**.

### Scenario B: Active Log Upload (Chapter 8)
This scenario tests the app's ability to "push" a log upload notification to CasHUB using a Broadcast Intent.

1. **Preparation:** Ensure a log file exists (run Scenario A first).
2. **Trigger:** Click **"2. Trigger Active Upload"** in the app.
3. **Action:** The app sends the `cashub.active.upload` intent with the file path and package name as extras.
4. **Feedback:**
    * **Success:** App displays `CasHUB Status: SUCCESS` (Result Code 0).
    * **Failure:** App displays the specific error code returned by the CasHUB Agent.
5. **Verification:** Check the **Application Log** page in the CasHUB Portal to view the uploaded entry.

---

## 3. Troubleshooting

* **File Not Found:** * Ensure the directory `/mnt/sdcard/cashub_demo` exists in Device Explorer.
    * Confirm the app has been granted **"All Files Access"** in the Android System Settings.
* **Empty CSV File:**
    * The app requires a brief moment to dump the `logcat` buffer.
    * The app includes a "Manual Test Log" entry to ensure the file is never empty upon creation.
* **Intent Failure:**
    * Verify the **CasHUB Agent** is active on the terminal.
    * Ensure the terminal is properly bound to the CasHUB environment.