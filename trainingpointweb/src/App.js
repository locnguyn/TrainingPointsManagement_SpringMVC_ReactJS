import React, { useEffect, useReducer, useRef } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Container } from "react-bootstrap";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/Common/Header";
import Footer from "./components/Common/Footer";
import Activity from "./components/Activity/Activity";
import Login from "./components/User/Login";
import Register from "./components/User/Register";
import Home from "./components/Common/Home";
import { MyDispatcherContext, MyUserContext } from "./configs/Contexts";
import MyUserReducer from "./components/Reducer/UserReducer";
import cookie from 'react-cookies';
import User from "./components/User/User";
import Report from "./components/Activity/ReportMissing";
import Password from "./components/User/Password";
import ActivityDetails from "./components/Activity/ActivityDetails";
import Verify from "./components/User/Verify";
import DismissableToast from "./components/Common/Toast";
import { auth, encodeEmail, setOffline, updateUserData } from "./configs/firebase";
import ChatboxList from "./components/ChatboxList/ChatboxList";
import './App.css';

const UPDATE_INTERVAL_MS = 10000;

function App() {
  const [user, dispatch] = useReducer(MyUserReducer, cookie.load("user") || null);
  const lastUpdateRef = useRef(Date.now());

  useEffect(() => {

    const updateUserStatus = async () => {
      const now = Date.now();
      const user = auth.currentUser;
      if (user && now - lastUpdateRef.current > UPDATE_INTERVAL_MS) {
        lastUpdateRef.current = now;
        updateUserData(encodeEmail(user.email), {
          status: "online",
          lastActive: new Date(),
        });
      }
    };

    const handleBeforeUnload = (e) => {
      e.preventDefault();
      setOffline();
      e.returnValue = true;
    };

    const intervalId = setInterval(updateUserStatus, UPDATE_INTERVAL_MS);
    window.addEventListener('beforeunload', handleBeforeUnload);
    document.addEventListener('visibilitychange', updateUserStatus);

    return () => {
      clearInterval(intervalId);
      window.removeEventListener('beforeunload', handleBeforeUnload);
      document.removeEventListener('visibilitychange', updateUserStatus);
    };
  }, []);

  return (
    <Container className="App">
      <BrowserRouter>
        <MyUserContext.Provider value={user}>
          <MyDispatcherContext.Provider value={dispatch}>
            <Header />
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/activity" element={<Activity />} />
              <Route path="/report_missing" element={<Report />} />
              <Route path="/activity/:activityId" element={<ActivityDetails />} />
              <Route path="/verify" element={<Verify />} />
              <Route path="/register" element={<Register />} />
              <Route path="/login" element={<Login />} />
              <Route path="/current-user" element={<User />} />
              <Route path="/change_password" element={<Password />} />
              <Route path="/activity/:activityId" element={<ActivityDetails />} />
            </Routes>
            <Footer />
            {user && <ChatboxList />}
          </MyDispatcherContext.Provider>
        </MyUserContext.Provider>
      </BrowserRouter>
      <DismissableToast />
    </Container>
  );
}

export default App;
