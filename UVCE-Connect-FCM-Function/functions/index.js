
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// for news feed.
exports.NewsFeedNotification = functions.database.ref('/News/{id}').onCreate(event => {
    const payload = {
        notification : {
            title : 'News',
            body : 'A new post was added!',
            sound : 'default',
            tag : 'News Feed',
        },
        data: {
            "Feed" : "News"
        }
    };

    const options = {
        priority : 'high',
        timeToLive : 60 * 60 * 24
    };

    return admin.messaging().sendToTopic('TEST', payload, options)
                            .then(function(response) {
                                console.log('News feed notification SUCCESSFULL'
                                            , event.params.id, response);
                            })
                            .catch(function(error) {
                                console.log('News feed notification FAILED'
                                            , event.params.id, error);
                            });
});

// for campus says.
exports.CampusSaysNotification = functions.database.ref('/Campus Says/{id}').onCreate(event => {
    const payload = {
        notification : {
            title : 'Campus Says',
            body : 'A new post was added!',
            sound : 'default',
            tag : 'Campus Says',
        },
        data : {
            "Feed" : "Campus Says"
        }
    };

    const options = {
        priority : 'high',
        timeToLive : 60 * 60 * 24
    };

    return admin.messaging().sendToTopic('TEST', payload, options)
                            .then(function(response) {
                                console.log('Campus Says notification SUCCESSFULL'
                                            , event.params.id, response);
                            })
                            .catch(function(error) {
                                console.log('Campus Says notification FAILED'
                                            , event.params.id, error);
                            });
});
