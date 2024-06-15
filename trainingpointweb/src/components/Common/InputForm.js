import React, { useEffect, useRef, useState } from 'react';
import { IoSend } from "react-icons/io5";
import { MdOutlineEmojiEmotions } from "react-icons/md";
import EmojiPicker from 'emoji-picker-react';

const InputForm = ({ handleSubmit, value = '', placeholder, onChange = () => { }, autoFocus = false, onBlur = () => { } }) => {
    const [inputValue, setInputValue] = useState(value);
    const [showEmojiPicker, setShowEmojiPicker] = useState(false);
    const [showButton, setShowButton] = useState(false);
    const emojiRef = useRef(null);
    const emojiPickerRef = useRef(null);
    const inputRef = useRef(null);
    const MAX_ROW = 3;

    const handleClickOutside = (event) => {
        if (emojiPickerRef.current && !emojiPickerRef.current.contains(event.target)) {
            setShowEmojiPicker(false);
        }
        if (emojiRef.current && !emojiRef.current.contains(event.target)
            // && emojiPickerRef.current && !emojiPickerRef.current.contains(event.target)
            && inputRef.current && !inputRef.current.contains(event.target)) {
            // handleSubmit();
            // onBlur();
        }
    };

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleChange = (e) => {
        const el = e.target;
        if (isScrollable(el) && MAX_ROW > el.rows) {
            el.rows = el.rows + 1;
        }
        if (!el.value) {
            el.rows = 1;
        }
        setInputValue(el.value);
        console.log(el.value);
        onChange(el.value);
    };

    const handleEmojiClick = (emojiObject, event) => {
        setInputValue(prevContent => prevContent + emojiObject.emoji);
        onChange(prevContent => prevContent + emojiObject.emoji);
    };

    function isScrollable(el) {
        return el.scrollWidth > el.clientWidth || el.scrollHeight > el.clientHeight;
    }

    return (
        <>
            <div className='d-flex flex-column' style={{width: "100%", alignItems: 'center', borderRadius: '16px', backgroundColor: 'rgb(237, 237, 237)', padding: '5px', position: 'relative' }}>
                <textarea
                    id='my-textarea'
                    type="text"
                    value={inputValue}
                    onChange={handleChange}
                    onFocus={() => setShowButton(true)}
                    onKeyDown={(e) => {
                        if(e.key === 'Enter' && inputValue !== "" && !e.shiftKey) {
                            e.preventDefault();
                            e.target.value = "";
                            e.target.rows = 1;
                            setInputValue("");
                            handleSubmit();
                        }
                    }}
                    placeholder={placeholder ? placeholder : 'Nhập...'}
                    rows={1}
                    resize='none'
                    style={{
                        padding: '10px',
                        margin: '0px 10px',
                        // borderRadius: '4px',
                        border: 'none',
                        backgroundColor: 'rgb(237, 237, 237)',
                        resize: 'none',
                        overflow: 'auto',
                        width: '100%'
                    }}
                    autoFocus={autoFocus}
                    ref={inputRef}
                />
                {showButton &&
                    <div className='d-flex justify-content-between' style={{ width: "100%" }}>
                        <span onClick={() => setShowEmojiPicker(!showEmojiPicker)} role='button' ref={emojiRef}>
                            <MdOutlineEmojiEmotions />
                        </span>
                        <button
                            onClick={() => {
                                handleSubmit();
                                setInputValue('');
                            }}
                            style={{
                                borderRadius: '4px',
                                border: 'none',
                                cursor: 'pointer',
                                color: 'primary',
                            }}
                            disabled={inputValue === ""}
                            className={inputValue !== "" ? "text-primary" : ""}
                        >
                            <div>
                                <IoSend />
                            </div>
                        </button>
                    </div>}
                <div style={{ position: 'absolute', zIndex: 1, top: '-600%' }}>
                    {showEmojiPicker && (
                        <div ref={emojiPickerRef} >
                            <EmojiPicker onEmojiClick={handleEmojiClick}/>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
};

export default InputForm;
