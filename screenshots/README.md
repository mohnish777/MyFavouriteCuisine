# 📸 Screenshots Guide

This folder contains screenshots of the MyFavouriteCuisine app for documentation purposes.

## 📋 Required Screenshots

Please capture the following screens and save them with the exact names listed below:

### Main Screens
1. **splash_screen.png** - The animated splash screen
2. **all_dishes.png** - Main screen showing all dishes in grid layout
3. **favorites.png** - Favorite dishes screen
4. **random_dish.png** - Random dish screen with API data

### Features
5. **dish_details.png** - Detailed view of a dish with ingredients and cooking time
6. **add_dish.png** - Add/Edit dish screen with form fields
7. **filter_dialog.png** - Filter dialog showing dish types
8. **notification.png** - Notification screenshot (can be from notification shade)

### Optional (Nice to have)
- **image_selection_dialog.png** - Camera/Gallery selection dialog
- **permissions_dialog.png** - Permission request dialog
- **delete_confirmation.png** - Delete confirmation dialog
- **app_icon.png** - App icon/launcher icon

## 🎯 How to Capture Screenshots

### Method 1: Using Android Studio (Recommended)
1. Run the app on an emulator or connected device
2. Navigate to the screen you want to capture
3. Click the **Camera icon** 📷 in the emulator toolbar (or use the Logcat window)
4. Save the screenshot to this folder with the appropriate name

### Method 2: Using Device/Emulator
1. **Android Emulator**: Click the camera icon in the toolbar
2. **Physical Device**: Press `Power + Volume Down` simultaneously
3. Transfer the screenshot to this folder and rename it

### Method 3: Using ADB
```bash
# Take screenshot
adb shell screencap -p /sdcard/screenshot.png

# Pull to computer
adb pull /sdcard/screenshot.png screenshots/
```

## 📐 Screenshot Guidelines

### Best Practices:
- ✅ Use a **clean device** (no personal data visible)
- ✅ Use **consistent device** (same phone/emulator for all screenshots)
- ✅ Capture in **portrait mode** (unless landscape is required)
- ✅ Use **light theme** for better visibility
- ✅ Show **realistic data** (not empty states unless demonstrating that feature)
- ✅ Recommended resolution: **1080x1920** or **1440x2960**

### What to Avoid:
- ❌ Don't include status bar with personal info (time, battery, notifications)
- ❌ Don't use screenshots with debug overlays
- ❌ Don't include screenshots with Lorem Ipsum or test data
- ❌ Avoid blurry or low-quality images

## 🎨 Optional: Create a Banner

You can create an attractive banner image showing multiple screens:
1. Use tools like **Figma**, **Canva**, or **Adobe XD**
2. Combine 3-4 key screenshots in a mockup
3. Add app name and tagline
4. Save as `app_banner.png`

### Online Tools:
- [Mockuphone](https://mockuphone.com/) - Device mockups
- [Canva](https://www.canva.com/) - Design tool
- [Figma](https://www.figma.com/) - Professional design tool

## 📝 After Adding Screenshots

1. Make sure all images are in this folder
2. The README.md will automatically display them
3. Commit the screenshots to Git:
   ```bash
   git add screenshots/
   git commit -m "docs: Add app screenshots"
   ```

## 🔍 File Naming Convention

Use lowercase with underscores:
- ✅ `splash_screen.png`
- ✅ `all_dishes.png`
- ❌ `SplashScreen.png`
- ❌ `all-dishes.png`

---

**Note:** Screenshots should be in PNG format for best quality. Keep file sizes reasonable (< 500KB each) by using appropriate compression.

