# CasHUB Log Tester (Demo App)

This application is a specialized tool designed to verify the **Log Upload** features of the **CasHUB platform**, specifically supporting Manual Upload, Active Upload, and Scheduled Active Upload.

---

## 1. Project Configuration
To ensure compatibility with the CasHUB portal, the following parameters are used:

* **Folder Path:** `/sdcard/cashub_demo` (Standard External Storage)
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
    * **File Path:** `/sdcard/cashub_demo`
4. **Execute:** Click **CREATE**.
5. **Result:** Monitor the status until it transitions to **"Uploaded"**.

### Scenario B: Active Log Upload (Chapter 8)
This scenario tests the app's ability to "push" a log upload notification to CasHUB using a Broadcast Intent.

1. **Preparation:** Ensure a log file exists (run Scenario A first).
2. **Trigger:** Click **"2. Trigger Active Upload"** in the app.
3. **Action:** The app sends the `cashub.active.upload` intent with the file path and package name as extras.
4. **Result:** Check the **Application Log** page in the CasHUB Portal.

### Scenario C: Scheduled Active Upload (Automatic)
This scenario verifies that the app can automatically push logs to CasHUB on a schedule, behaving exactly like Scenario B but triggered by the system.

1. **Background Task:** The app uses `WorkManager` to automatically capture **full logs** and trigger an **Active Upload** every 24 hours.
2. **Instant Test:** For testing purposes, an upload is also triggered **immediately upon app restart**.
3. **Automation:** The `LogWorker` saves the log file (overwriting with fresh data) and sends the `cashub.active.upload` broadcast automatically.
4. **Verification:** Check the **Application Log** page in the CasHUB Portal to see the entries uploaded automatically by the background worker.

---

## 3. Troubleshooting

* **Error 28 (File Not Found):** 
    * Ensure the app has been granted **"All Files Access"** in the Android System Settings. Go to **Settings > Apps > Special app access > All files access** and enable it for **CasHUB Log Tester**.
    * Verify the directory `/sdcard/cashub_demo` exists in Device Explorer.
* **Empty CSV File:**
    * The app includes a "Manual Test Log" entry to ensure the file is never empty upon creation.
* **Intent Failure:**
    * Verify the **CasHUB Agent** is active on the terminal and has necessary permissions.
