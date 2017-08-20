const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// for news feed.
exports.NewsFeedNotification = functions.database.ref('/News/{id}').onCreate(event => {
    const payload = {
        notification : {
            title : 'UVCE Connect',
            body : 'A new post was added in News feed',
            sound : 'default'
        },
        data: {
            "Feed" : "News"
        }
    };

    const options = {
        priority : 'normal',
        timeToLive : 60 * 60 * 24
    };

    return admin.messaging().sendToTopic('UVCE', payload, options)
                            .then(function(response) {
                                console.log('News feed notification SUCCESSFULL'
                                            , event.params.id, response);
                            })
                            .catch(function(error) {
                                console.log('News feed notification FAILED'
                                            , error);
                            });
});

// for campus says.
exports.CampusSaysNotification = functions.database.ref('/Campus Says/{id}').onCreate(event => {
    const payload = {
        notification : {
            title : 'UVCE Connect',
            body : 'A new post was added in Campus Says',
            sound : 'default'
        },
        data : {
            "Feed" : "Campus Says"
        }
    };

    const options = {
        priority : 'high',
        timeToLive : 60 * 60 * 24
    };

    return admin.messaging().sendToTopic('UVCE', payload, options)
                            .then(function(response) {
                                console.log('Campus Says notification SUCCESSFULL'
                                            , event.params.id, response);
                            })
                            .catch(function(error) {
                                console.log('Campus Says notification FAILED'
                                            , error);
                            });
});
