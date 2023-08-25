# notification-service
Customised service to send out emails and app notifications

## cURL for sending PUSH notifications
API endpoint to send out push notification to registered devices using Google Firebase.

> [!IMPORTANT]
> Before deploying this service in any environment, one should provide Firebase JSON config.

```
### Sample Request
curl --location '{URL}/api/notification/send' \
--header 'Content-Type: application/json' \
--data '{
    "title":"notification",
    "body":"successfully we sent notification",
    "deviceToken":"dfyBA3tIXcbkTuFcXvlIZB:APA91bGik1lrcpNqI7fE5cIOGetsnX-s-wPQ3X76jwfuf-KfxlVgoG0okb-wub6wNeAsdW_vS8vQGMgTVknGsazTO6Z0hcGqeHKCHiBDyEbZUOhm4NVxueeZCs9oA2qcP2Yp0wWX4ece",
    "userId": 2
}'
```

#### Request body attributes
* **title** - Takes the title of the notification
  * type - String
* **body** - Takes content of the notification
  * type - String 
* **deviceToken** - Takes registered device token ID
  * type - String 
* **userId** -  Takes the user's ID to whom notification is sent
  * type - String 
