import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-analytics.js";
import { getDatabase, ref, push, set, onChildAdded, child, onValue, get } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-database.js";
import { getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";

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

export const setUserData = (userId, data) => {
    const userRef = ref(database, 'users/' + userId);
    onValue(userRef, (snapshot) => {
        if (!snapshot.exists()) {
            set(userRef, { ...data }, {merge: true});
        }
    });
}

export const getAssistantList = () => {
    const usersRef = ref(database, 'users');
    const snapshot = get(usersRef);
    const assistants = [];
    snapshot.forEach((childSnapshot) => {
        const userData = childSnapshot.val();
        if (userData.user_role === 'ROLE_ASSISTANT') {
            assistants.push(userData);
        }
    });
    return assistants;
}

export { auth, database, ref, push, set, onChildAdded, child, onValue, signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut };
