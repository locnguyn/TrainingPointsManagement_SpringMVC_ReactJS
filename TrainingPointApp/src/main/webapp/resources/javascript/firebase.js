import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-analytics.js";
import { getDatabase, remove, ref, set, onValue, update, off, push, child, onChildAdded } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-database.js";
import { getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut, updatePassword } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";

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

const encodeEmail = (email) => {
    return btoa(email).replace(/\./g, '%2E');
};

const setUserData = (userId, data) => {
    const userRef = ref(database, 'users/' + userId);
    set(userRef, { ...data }, { merge: true });
};

const updateUserData = (userId, data) => {
    const userRef = ref(database, 'users/' + userId);
    update(userRef, { ...data });
};

const handleRegisterFirebase = async (email, password, role, firstName, lastName, avatar) => {
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
    } catch (error) {
        console.error(error);
        throw error;
    }
};
