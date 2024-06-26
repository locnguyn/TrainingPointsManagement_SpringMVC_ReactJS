import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-analytics.js";
import { getDatabase, remove, ref, set, onValue, update, off, push, child, onChildAdded } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-database.js";
import { getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut, updatePassword } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";
import moment from 'moment';

moment.locale("vi");

const firebaseConfig = {
    apiKey: "AIzaSyAIwE31BVcsslLRG7twverchPX3WoDd4s8",
    authDomain: "trainingpointschat.firebaseapp.com",
    databaseURL: "https://trainingpointschat-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "trainingpointschat",
    storageBucket: "trainingpointschat.appspot.com",
    messagingSenderId: "899435695935",
    appId: "1:899435695935:web:2bb89f9dcd0e971df18d8a",
    measurementId: "G-MKBGZRPB5T"
};

const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const database = getDatabase(app);
const auth = getAuth(app);

export const encodeEmail = (email) => {
    return btoa(email).replace(/\./g, '%2E');
}

export const decodeEmail = (encodedEmail) => {
    const base64Email = encodedEmail.replace(/%2E/g, '.');
    return atob(base64Email);
}

export const getAssistantList = (callback, email) => {
    const usersRef = ref(database, 'users');
    let chats = [];
    getMyChattingUser((c)=>{
        chats = c;
    }, email)
    console.log(chats, email);
    const listener = onValue(usersRef, (snapshot) => {
        const assistants = [];
        snapshot.forEach((childSnapshot) => {
            const userData = childSnapshot.val();
            if (userData.user_role === 'ROLE_ASSISTANT' || chats.includes(userData.email)) {
                assistants.push(userData);
            }
        });
        callback(assistants);
    });
    return () => off(usersRef, listener);
};

export const getMyChattingUser = (callback, email) => {
    console.log('getMyChattingUser')
    const chatsRef = ref(database, 'users/' + encodeEmail(email) + '/chats');
    const listener = onValue(chatsRef, (snapshot) => {
        const chats = [];
        snapshot.forEach((childSnapshot) => {
            const email = childSnapshot.val().email;
            console.log(childSnapshot.val());
            chats.push(email);
        });
        callback(chats);
    });
    return () => off(chatsRef, listener);
};

export const getChatId = (userEmail) => {
    const user1 = encodeEmail(auth.currentUser.email);
    const user2 = encodeEmail(userEmail);
    const chatId = user1 < user2 ? user1 + "_" + user2 : user2 + "_" + user1;
    return chatId;
}

export const getMessages = (callback, userEmail) => {
    const chatId = getChatId(userEmail);
    const messagesRef = ref(database, 'chats/' + chatId + '/messages');
    const listener = onValue(messagesRef, (snapshot) => {
        const messages = [];
        snapshot.forEach((message) => {
            messages.push(message.val());
        });
        callback(messages);
    });

    return () => off(messagesRef, listener);
}

export const messageListener = (callback, userEmail) => {
    const chatId = getChatId(userEmail);
    const messagesRef = ref(database, 'chats/' + chatId + '/messages');
    const listener = onChildAdded(messagesRef, (snapshot) => {
        callback([snapshot.val()]);
    });

    return () => off(messagesRef, listener);
}

export const chatListener = (callback, userEmail) => {
    const chatsRef = ref(database, 'chats');
    const uid = encodeEmail(userEmail);

    const listener = onChildAdded(chatsRef, (snapshot) => {
        if (snapshot.key.includes(uid))
            callback((pre) => [...pre, getUserInfoByEmail(decodeEmail(snapshot.key))]);
    });

    return () => off(chatsRef, listener);
}

export const listenToAllChats = (callback, currentUserEmail) => {
    const encodedCurrentUserEmail = encodeEmail(currentUserEmail);
    console.log(currentUserEmail)
    const chatsRef = ref(database, 'chats');

    const chatsListener = onValue(chatsRef, (snapshot) => {
        snapshot.forEach((childSnapshot) => {
            const chatId = childSnapshot.key;
            if (chatId.includes(encodedCurrentUserEmail)) {
                const messagesRef = ref(database, `chats/${chatId}/messages`);
                const messagesListener = onChildAdded(messagesRef, (snapshot) => {
                    if (snapshot.val().email !== currentUserEmail) {
                        callback(snapshot.val());
                    }
                });
                return () => off(messagesRef, messagesListener);
            }
        });
    });

    return () => off(chatsRef, chatsListener);
};

export const getUserInfoByEmail = (email) => {
    return new Promise((resolve, reject) => {
        const id = encodeEmail(email);
        const userRef = ref(database, `users/${id}`);
        onValue(userRef, (snapshot) => {
            if (snapshot.exists()) {
                resolve(snapshot.val());
            } else {
                resolve(null); // Resolve with null if the user does not exist
            }
        }, reject);
    });
}

export const listenNewNotifications = (email, callback) => {
    const notiRef = ref(database, 'users/' + encodeEmail(email) + '/notifications');

    const listener = onChildAdded(notiRef, (snapshot) => {
        const notification = snapshot.val();
        console.log(notification);
        callback(notification); // Pass notification to callback
    });

    return () => off(notiRef, listener);
};


export const readMessage = (email, sender) => {
    const notiRef = ref(database, 'users/' + encodeEmail(email) + '/notifications/' + encodeEmail(sender));
    const listener = onValue(notiRef, async (snapshot) => {
        if (snapshot.exists()) {
            try {
                await remove(notiRef);
                console.log(`Notification from ${sender} for ${email} removed successfully.`);
            } catch (error) {
                console.error(`Failed to remove notification from ${sender} for ${email}: `, error);
            }
        }
    });

    // Clean up listener after the operation
    return () => {
        off(notiRef, listener);
    }
};

export const sendMessage = async (chatId, message, email, recipient) => {
    // Remove notification for the recipient
    const stopReadingMessage = readMessage(email, recipient);

    // Proceed with sending the message
    const id = push(child(ref(database), 'chats/' + chatId + '/messages')).key;
    await set(ref(database, 'chats/' + chatId + '/messages/' + id), {
        email: email,
        message: message
    });

    // Set a new notification for the recipient
    const notiRef = ref(database, 'users/' + encodeEmail(recipient) + '/notifications/' + encodeEmail(email));
    await set(notiRef, {
        email: email,
    });

    await startChat(email, recipient);
    // Stop the notification listener
    stopReadingMessage();
};

export const startChat = async (email, recipient) => {
    let chattingRef = ref(database, 'users/' + encodeEmail(recipient) + '/chats/' + encodeEmail(email));
    await set(chattingRef, {
        email: email,
    });

    chattingRef = ref(database, 'users/' + encodeEmail(email) + '/chats/' + encodeEmail(recipient));
    await set(chattingRef, {
        email: recipient,
    });
};

export const setUserData = (userId, data) => {
    const userRef = ref(database, 'users/' + userId);
    set(userRef, { ...data }, { merge: true });
};

export const updateUserData = (userId, data) => {
    console.log(data);
    const userRef = ref(database, 'users/' + userId);
    update(userRef, { ...data });
};

export const updatePasswordFirebase = async (newPassword) => {
    try {
        const auth = getAuth();
        const user = auth.currentUser;

        if (!user) {
            throw new Error("User is not authenticated.");
        }

        await updatePassword(user, newPassword);
        console.log("Password updated successfully.");
    } catch (error) {
        console.error("Error updating password:", error);
        throw error;
    }
};

export const handleRegisterFirebase = async (email, password, role, firstName, lastName, avatar) => {
    try {
        await createUserWithEmailAndPassword(auth, email, password);
        const uid = encodeEmail(email);
        setUserData(uid, {
            user_role: role,
            status: "offline",
            lastActive: new Date(),
            email: email,
            firstName: firstName,
            lastName: lastName,
            avatar: avatar
        });
        // signOut(auth);
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const handleLoginFirebase = async (email, password, userInfo) => {
    console.log(userInfo)
    try {
        const userCredential = await signInWithEmailAndPassword(auth, email, password);
        const user = userCredential.user;
        updateUserData(encodeEmail(user.email), {
            status: "online",
            lastActive: new Date(),
            avatar: userInfo.avatar,
            firstName: userInfo.firstName,
            lastName: userInfo.lastName,
            user_role: userInfo.role,
        });
    } catch (e) {
        console.error(e);
        throw e;
    }
};

export const setOffline = () => {
    const user = auth.currentUser;
    if (user) {
        updateUserData(encodeEmail(user.email), {
            status: "offline",
            lastActive: new Date()
        });
    }
};

export { auth, database, ref, set, onValue, signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut };
