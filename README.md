# Chores Manager

Simple app to manage household chores reminders with a different organization than traditional reminders.

## Guest mode and cloud sync

- The app now supports two access modes:
  - **Guest**: uses the local Room database only.
  - **Authenticated user**: uses backend APIs for chores and keeps an in-memory local cache for UI rendering.
- Authenticated users can login with **username + password** and access synchronized chores across devices.

## Backend (Spring Boot)

A backend service is included in `/backend`.

### Run backend locally

```bash
cd backend
mvn spring-boot:run
```

The Android app is configured to call `http://10.0.2.2:8080/` (Android emulator host loopback).

## Try it out

You can download it from the [Google Play Store](https://play.google.com/store/apps/details?id=com.colombo.choresmanager).
