import React, { useState, useEffect, useContext, useRef } from 'react';
import './ChatboxList.css';
import Chatbox from '../Chatbox/Chatbox';
import { chatListener, getAssistantList, getUserInfoByEmail, listenNewNotifications, listenToAllChats, readMessage, set, startChat } from '../../configs/firebase';
import { FaFacebookMessenger } from 'react-icons/fa6';
import { FcAssistant } from "react-icons/fc";
import { MyUserContext } from '../../configs/Contexts';
import { Image } from 'react-bootstrap';
import moment from 'moment';
import { FaSearch } from "react-icons/fa";

moment.locale('vi');

const ChatboxList = () => {
    const [users, setUsers] = useState([]);
    const [openChatboxes, setOpenChatboxes] = useState([]);
    const [showChatboxes, setShowChatboxes] = useState(false);
    const [isInputExpanded, setIsInputExpanded] = useState(false);
    const user = useContext(MyUserContext);
    const inputRef = useRef(null);


    useEffect(() => {
        const initializeListeners = async () => {
            // Example of fetching assistant list asynchronously
            const unsubscribe = getAssistantList(setUsers, user.userInfo.email);

            // Set up notification listener
            const unsubscribeNotiListen = listenNewNotifications(user.userInfo.email, (notification) => {
                console.log('New notification:', notification);
                // Handle notification, e.g., open chatbox if needed
                openChatBox(notification.email);
            });

            return () => {
                // Unsubscribe listeners on cleanup
                unsubscribe();
                unsubscribeNotiListen();
            };
        };

        initializeListeners();
    }, []);


    const toggleChatboxList = () => {
        console.log(users)
        setOpenChatboxes([]);
        setShowChatboxes(!showChatboxes);
    }

    const toggleChatbox = async (id) => {
        await startChat(user.userInfo.email, id);
        readMessage(user.userInfo.email, id);
        if (users.find(a => a.email === id))
            setOpenChatboxes((prevOpenChatboxes) => {
                if (prevOpenChatboxes.includes(id)) {
                    return prevOpenChatboxes.filter(chatboxId => chatboxId !== id);
                } else {
                    return [...prevOpenChatboxes, id];
                }
            });
    };

    const openChatBox = async (id) => {
        try {
            const userInfo = await getUserInfoByEmail(id);
            if (userInfo) {
                await startChat(user.userInfo.email, id);
                readMessage(user.userInfo.email, id);
                setUsers((prevUsers) => {
                    // Add user if not already in the list
                    if (!prevUsers.find(user => user.email === id)) {
                        return [...prevUsers, userInfo];
                    }
                    return prevUsers;
                });

                setOpenChatboxes((prevOpenChatboxes) => {
                    // Add chatbox if not already open
                    if (!prevOpenChatboxes.includes(id)) {
                        return [...prevOpenChatboxes, id];
                    }
                    return prevOpenChatboxes;
                });
            } else {
                console.warn(`No user found with email ${id}`);
            }
        } catch (error) {
            console.error("Failed to fetch user info:", error);
        }
    };

    const handleInputClick = () => {
        setIsInputExpanded(true);
        if (inputRef.current) {
            inputRef.current.focus();
        }
    };


    return (
        <div className={`chatbox-list-container ${showChatboxes ? 'open' : ''}`}>
            <div className="open-chatboxes">
                {openChatboxes.map((id) => {
                    const u = users.find(a => a.email === id);
                    return (
                        <Chatbox
                            key={id}
                            isOpen={true}
                            toggleChatbox={() => toggleChatbox(id)}
                            chattingUser={u}
                        />
                    );
                })}
            </div>
            <div className="chatbox-thumbnail-list">
                {users && users.length > 0 && users.map((u, index) => {
                    if (u.email !== user.userInfo.email) {
                        return (<div
                            key={index}
                            className={`chatbox-thumbnail ${showChatboxes ? 'open' : ''}`}
                            onClick={() => toggleChatbox(u.email)}
                        >
                            {u && u.avatar ? (
                                <Image src={u.avatar} style={{
                                    width: "50px",
                                    height: "50px",
                                    objectFit: "cover",
                                }}
                                    roundedCircle />
                            ) : (
                                u.firstName
                            )}
                            {u.status === 'online' && moment(u.lastActive).fromNow() === "vài giây trước" && (
                                <div className="online-indicator"></div>
                            )}
                            <div className='assistant-icon'>
                                {u.user_role === "ROLE_ASSISTANT" ? <FcAssistant size={25} /> : <></>}
                            </div>
                            <span>{u.firstName + " - " + u.email}</span>
                        </div>);
                    }
                    return null;
                })}
                <div
                    className={`chatbox-thumbnail open ${isInputExpanded ? 'expanded' : ''}`}
                    onClick={handleInputClick}
                    onBlur={() => setIsInputExpanded(false)}
                    ref={inputRef}
                >
                    {isInputExpanded ? (
                        <div className='d-flex flex-row p-3'>
                            <input
                                type='email'
                                placeholder='Nhập email người nhận'
                                onKeyUp={(e) => {
                                    if (e.key === 'Enter') {
                                        openChatBox(e.target.value);
                                        setIsInputExpanded(false);
                                    }
                                }}
                            />
                            <FaSearch size={20} />
                        </div>
                    ) : (
                        <FaSearch size={20} />
                    )}
                </div>
                <div
                    className="chatbox-thumbnail open"
                    onClick={() => toggleChatboxList()}
                >
                    <FaFacebookMessenger size={20} />
                </div>
            </div>
        </div>
    );
};

export default ChatboxList;
