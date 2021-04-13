# Time Manger

## Compatibility

An Android device or emulator is required to test or play this game. Player are therefore expected to have either.

- Platform:
    - Please compile with: Android Studio 4.1.1 & Java 1.8

- Supported API levels:
    - **Android 8, API Level 27 or above**

- Permission requirement:
    - Access device location

***

## Function & Features

- Add schedule
  - The application can record upcoming events. The content of the event includes reminders about the event, who happened with the event and the time when the event occurred.

- Add notification
    - Users can freely set notification reminders for events. The user can set the number of notification reminders and the interval between notifications.

- Add mood
    - Users can add the mood or diary of the day to record their life.

- Weather
    - The application will display weather information based on the user's location.

***

## Test Cases

- test case1: **Add schedule**
    - **Step 1** When the user selects a date in the calendar interface, the user can smoothly switch the UI to the add event interface.
    - **Step 2** User can add event information according to the interface prompts, including the name of the event, the person of the event, and the time of the event.
    - **step 3** After the user successfully adds the event, user can view the event details on the home page


- test case2: **Add notification**
    - **Step 1** When the user selects a date in the calendar interface, the user can smoothly switch the UI to the add event interface.
    - **Step 2** After users add event information, they can choose to add notification options.
    - **step 3** On the screen, there will be show the settings for notifications. Users can freely set the notification interval and the number of notification reminders.
    - **step 4** When the event occurs, the user will receive one or more notifications according to the notification settings.


- test case3: **Add mood**
    - **Step 1** When the user selects a date in the calendar interface, the user can smoothly switch the UI to the add mood interface.
    - **Step 2** User can add mood information according to the interface prompts.
    - **step 3** After the user successfully adds the mood, user can view the mood details on the home page


- test case4: **Weather**
    - **Step 1** When the user selects the calendar interface, the user will see a weather information based on the user's location at the top of the screen.
    - **Step 2** If running on the emulator, when the user changes the virtual geographic location information, the weather information will also change.

- I hope that when you open our software, you can select a date, and then click the date to test whether you can add events, add dates, and get the weather of your current location. At the same time, we also have the alarm function. You can also change the time interval of the alarm and the number of reminders
    
***

## Bug

- **Bug 1** 
    - The function about weather is not found in the api which provided by the system.
    - **fixed**: Find the api provided by the third party to get weather information.

- **Bug 2**
    - App crashes when calling api to get weather information.
    - **Fixed**: The function returns the weather data type as folat, so the variable type is changed from int to float.

- **Bug 3**
    - When calling the system alarm function as an event notification, when closing the application, user can not receiv the notification.
    - **Fixed**: Call the system notification function to notify when an event occurs.

***

## Issue

- Sometimes the deletion function cannot be completely deleted.
- The interface is not beautiful enough, it is not very high-end.
- We can't let users modify colors according to their own preferences.


***

## Changlog

- Please refer the release note

## About
- This application is a free application and developed by Team 8 with team members: Congkai Niu, Jialin Yang and Qianchao Wang. 
- If you have any problem regarding this application, please contact us via Class or Email.
