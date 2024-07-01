import React, { useContext, useEffect, useRef, useState } from 'react';
import moment from 'moment';
import { Image } from 'react-bootstrap';

import 'moment/locale/vi';
import { MyUserContext } from '../../configs/Contexts';
import InputForm from '../Common/InputForm';
import { authApi, endpoints } from '../../configs/APIs';

// Đặt locale tiếng Việt
moment.locale('vi');

const Menu = ({ onEdit, onDelete }) => (
    <div className=" dropdown-menu comment-dropdown-menu show">
        <button className="dropdown-item" onClick={onEdit}>Chỉnh sửa</button>
        <button className="dropdown-item" onClick={onDelete}>Xóa</button>
    </div>
);

const Comment = ({ comment, handleUpdate, handleDelete }) => {
    const [showMenu, setShowMenu] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [cmt, setCmt] = useState(comment);
    const {user} = useContext(MyUserContext);
    const svgRef = useRef(null);
    const menuRef = useRef(null);

    useEffect(() => {
        setCmt(comment);
      }, [comment]);

    const likeOrUnlike = async () => {
        try {
          const res = await authApi().post(endpoints['like-comment'](cmt.id));
          if (res.status === 200) {
            setCmt({
              ...cmt,
              liked: !cmt.liked,
              likes: cmt.liked ? cmt.likes - 1 : cmt.likes + 1
            })
          }
        } catch (ex) {
          console.error(ex);
        }
      }

    const toggleMenu = () => setShowMenu(!showMenu);
    const closeMenu = () => setShowMenu(false);
    const startEditing = () => {
        setIsEditing(true);
        closeMenu();
    };


    const handleUpdateComment = (content) => {
        handleUpdate(cmt.id, content);
        setIsEditing(false);
    };

    const handleClickOutside = (event) => {
        if (svgRef.current && !svgRef.current.contains(event.target)
            && menuRef.current && !menuRef.current.contains(event.target)) {
            closeMenu();
        }
    };

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <li className="mb-3" style={{ maxWidth: '50%' }}>
            <div className="d-flex">
                <Image
                    src={cmt.avatar}
                    width="30"
                    height="30"
                    className="me-2"
                    roundedCircle
                    alt={cmt.name}
                />
                {!isEditing ? <div className='d-flex flex-column' style={{ maxWidth: '100%' }}>
                    <div className='d-flex flex-row comment-wrapper'>
                        <div className="d-flex flex-column comment-content-wrapper p-2" style={{ flexGrow: 1, wordBreak: 'break-word', overflowWrap: 'break-word' }}>
                            <div>
                                <strong>{cmt.name}</strong>
                            </div>
                            <div>
                                {cmt.content}
                            </div>
                        </div>
                        {cmt.userId === user.userId ? (
                            <>
                                <div className='align-self-center' style={{ color: "light-gray" }} role='button' onClick={toggleMenu} ref={svgRef}>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-three-dots ms-1" viewBox="0 0 16 16">
                                        <path d="M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3" />
                                    </svg>
                                </div>

                                {showMenu && (
                                    <div ref={menuRef}>
                                        <Menu onEdit={startEditing} onDelete={() => handleDelete(cmt.id)} />
                                    </div>
                                )}
                            </>
                        ) : <></>}
                    </div>
                    <div className='d-flex flex-row justify-content-between'>
                        <div className='d-flex flex-row'>
                            <small className="text-muted ms-2 me-2">
                                {moment(cmt.createdAt).fromNow()}
                            </small>
                            <small role='button' onClick={likeOrUnlike} className={cmt.liked ? 'text-primary' : ''}>Thích</small>
                        </div>
                        {cmt.likes != 0 ? <div className='align-self-end ms-3' style={{height: "23px", marginTop: "-8px"}}><small style={{marginTop: "-8px"}}>{cmt.likes != 1 ? cmt.likes : ""}</small><small className='ms-1'>
                            <img style={{marginTop: "-4px"}} className="" height="18" role="presentation" src="data:image/svg+xml,%3Csvg fill='none' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3E%3Cpath d='M16.0001 7.9996c0 4.418-3.5815 7.9996-7.9995 7.9996S.001 12.4176.001 7.9996 3.5825 0 8.0006 0C12.4186 0 16 3.5815 16 7.9996Z' fill='url(%23paint0_linear_15251_63610)'/%3E%3Cpath d='M16.0001 7.9996c0 4.418-3.5815 7.9996-7.9995 7.9996S.001 12.4176.001 7.9996 3.5825 0 8.0006 0C12.4186 0 16 3.5815 16 7.9996Z' fill='url(%23paint1_radial_15251_63610)'/%3E%3Cpath d='M16.0001 7.9996c0 4.418-3.5815 7.9996-7.9995 7.9996S.001 12.4176.001 7.9996 3.5825 0 8.0006 0C12.4186 0 16 3.5815 16 7.9996Z' fill='url(%23paint2_radial_15251_63610)' fill-opacity='.5'/%3E%3Cpath d='M7.3014 3.8662a.6974.6974 0 0 1 .6974-.6977c.6742 0 1.2207.5465 1.2207 1.2206v1.7464a.101.101 0 0 0 .101.101h1.7953c.992 0 1.7232.9273 1.4917 1.892l-.4572 1.9047a2.301 2.301 0 0 1-2.2374 1.764H6.9185a.5752.5752 0 0 1-.5752-.5752V7.7384c0-.4168.097-.8278.2834-1.2005l.2856-.5712a3.6878 3.6878 0 0 0 .3893-1.6509l-.0002-.4496ZM4.367 7a.767.767 0 0 0-.7669.767v3.2598a.767.767 0 0 0 .767.767h.767a.3835.3835 0 0 0 .3835-.3835V7.3835A.3835.3835 0 0 0 5.134 7h-.767Z' fill='%23fff'/%3E%3Cdefs%3E%3CradialGradient id='paint1_radial_15251_63610' cx='0' cy='0' r='1' gradientUnits='userSpaceOnUse' gradientTransform='rotate(90 .0005 8) scale(7.99958)'%3E%3Cstop offset='.5618' stop-color='%230866FF' stop-opacity='0'/%3E%3Cstop offset='1' stop-color='%230866FF' stop-opacity='.1'/%3E%3C/radialGradient%3E%3CradialGradient id='paint2_radial_15251_63610' cx='0' cy='0' r='1' gradientUnits='userSpaceOnUse' gradientTransform='rotate(45 -4.5257 10.9237) scale(10.1818)'%3E%3Cstop offset='.3143' stop-color='%2302ADFC'/%3E%3Cstop offset='1' stop-color='%2302ADFC' stop-opacity='0'/%3E%3C/radialGradient%3E%3ClinearGradient id='paint0_linear_15251_63610' x1='2.3989' y1='2.3999' x2='13.5983' y2='13.5993' gradientUnits='userSpaceOnUse'%3E%3Cstop stop-color='%2302ADFC'/%3E%3Cstop offset='.5' stop-color='%230866FF'/%3E%3Cstop offset='1' stop-color='%232B7EFF'/%3E%3C/linearGradient%3E%3C/defs%3E%3C/svg%3E" width="18" />
                        </small></div>
                            : <></>}
                    </div>
                </div> : <InputForm value={cmt.content} autoFocus={true} handleSubmit={handleUpdateComment} onBlur={()=>setIsEditing(false)} />}
            </div>
        </li>
    );
}

const CommentList = ({ comments, handleUpdate, handleDelete }) => {
    const [localComments, setLocalComments] = useState(comments);

  useEffect(() => {
    setLocalComments(comments);
  }, [comments]);
    return (
    <ul id="commentId" className="mb-3 list-unstyled">
        {localComments &&
            localComments.length > 0 &&
            localComments.map((comment) => (
                <Comment key={comment.id} comment={comment} handleUpdate={handleUpdate} handleDelete={handleDelete} />
            ))}
    </ul>
)};

export default CommentList;
