
const admin = require('firebase-admin');
const functions = require('firebase-functions');
admin.initializeApp(functions.config().firebase);

// Listens for new messages added to messages/:pushId
exports.pushNotification = functions.database.ref('/notifications/{pushId}').onWrite( event => {

    console.log('Push notification event triggered');

    //  Grab the current value of what was written to the Realtime Database.
    var valueObject = event.data.val();

    console.log('the title is' + valueObject.campusName);
    console.log('the body is'  + valueObject.message );
  // Create a notification
    const payload = {
        notification: {
            title: valueObject.campusName ,
            body: valueObject.message
        }
    };

  //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

    return admin.messaging().sendToTopic("pushNotifications", payload, options);
});
