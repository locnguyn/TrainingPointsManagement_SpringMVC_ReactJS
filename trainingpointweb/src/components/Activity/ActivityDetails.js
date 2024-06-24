import { useContext, useEffect, useRef, useState } from "react";

import CommentList from "./CommentList";
import InputForm from "../Common/InputForm";
import toast from "react-hot-toast";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import { Button, Form, Image, Table } from "react-bootstrap";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { IoAdd } from "react-icons/io5";
import cookie from "react-cookies";
import MyModal from "../Common/Modal";

const ActivityDetails = () => {
  const [activities, setActivities] = useState([]);
  const [comments, setComments] = useState([]);
  const [reports, setReports] = useState([]);
  const { activityId } = useParams();
  const [comment, setComment] = useState("");
  const { user, userActivity, userReport } = useContext(MyUserContext);
  const { dispatchActivity, dispatchReport } = useContext(MyDispatcherContext);
  const location = useLocation();
  const nav = useNavigate();
  const [acPartType, setAcPartType] = useState();

  const [previewURL, setPreviewURL] = useState(null);
  const [show, setShow] = useState(false);
  const proof = useRef();

  const Close = () => {
    setShow(false);
    setPreviewURL(null);
  };
  const Show = (typeId) => {
    setShow(true);
    setAcPartType(typeId);
  };

  const fileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setPreviewURL(URL.createObjectURL(file));
    } else {
      setPreviewURL(null);
    }
  };

  const Load = async () => {
    try {
      let res = await authApi().get(endpoints["activity-details"](activityId));
      let re = await authApi().get(endpoints["user-report"]);

      if (res.status === 200 && re.status === 200) {
        setActivities(res.data);
        setComments(res.data.comments);
        setReports(re.data);
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  const addComment = async (comment) => {
    console.log(comment);
    try {
      let res = await authApi().post(
        endpoints["activity-add-comment"](activityId),
        { content: comment }
      );
      if (res.status === 201) {
        setComments([...comments, res.data]);
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  const deleteComment = async (commentId) => {
    try {
      const res = await authApi().delete(
        endpoints["delete-comment"](commentId)
      );
      if (res.status === 200) {
        setComments((prevComments) =>
          prevComments.filter((comment) => comment.id !== commentId)
        );
      }
    } catch (err) {
      toast.error("Lỗi khi xóa comment!");
      console.error(err);
    }
  };

  const updateComment = async (commentId, content) => {
    try {
      const res = await authApi().patch(
        endpoints["update-comment"](commentId),
        {
          content: content,
        }
      );
      if (res.status === 200) {
        setComments((prevComments) =>
          prevComments.map((comment) =>
            comment.id === commentId
              ? { ...comment, content: content }
              : comment
          )
        );
      }
    } catch (err) {
      toast.error("Lỗi khi cập nhật comment!");
      console.error(err);
    }
  };
  const activityRegistration = async (e, acPartTypeId) => {
    e.preventDefault();
    try {
      let res = await toast.promise(
        authApi().post(endpoints["create-registration"], {
          acPartTypeId: acPartTypeId,
        }),
        {
          loading: "Loading",
          success: "Đăng ký thành công",
          error: "Thất bại",
        }
      );

      if (res.status === 201) {
        dispatchActivity({
          type: "update-activities",
          payload: [...userActivity, res.data],
        });
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  useEffect(() => {
    if (user === null) {
      nav("/login", { state: { from: location } });
    } else {
      Load();
    }
  }, [user, nav, location]);

  const likeOrUnlike = async () => {
    console.log(user);
    try {
      const res = await authApi().post(endpoints["activity-like"](activityId));
      if (res.status === 200) {
        setActivities({
          ...activities,
          liked: !activities.liked,
          likes: activities.liked ? activities.likes - 1 : activities.likes + 1,
        });
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  const CreateReport = async (e, acPartTypedId, proof) => {
    e.preventDefault();

    try {
      let form = new FormData();
      form.append("acPartTypeId", acPartTypedId);
      console.log(form);
      if (proof) form.append("files", proof.current.files[0]);

      let res = await await toast.promise(
        authApi().post(endpoints["create-report"], form, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }),
        {
          loading: "Loading",
          success: "Báo thiếu thành công",
          error: "Thất bại",
        }
      );
      Close();

      dispatchReport({
        type: "update-reports",
        payload: [...userReport, res.data],
      });
    } catch (ex) {
      console.log(ex);
    }
  };

  return (
    <>
      {user !== null && (
        <>
          <h1 className="text-center mt-3">Chi Tiết Hoạt Động</h1>
          <Table
            responsive="sm"
            striped
            bordered
            hover
            className="activity-details"
          >
            <tbody>
              <tr>
                <td>
                  <strong>Tên Hoạt Động</strong>
                </td>
                <td>{activities.name}</td>
              </tr>
              <tr>
                <td>
                  <strong>Địa điểm</strong>
                </td>
                <td>{activities.location}</td>
              </tr>
              <tr>
                <td>
                  <strong>Thời gian bắt đầu</strong>
                </td>
                <td>{activities.startDateTime}</td>
              </tr>
              <tr>
                <td>
                  <strong>Thời gian kết thúc</strong>
                </td>
                <td>{activities.endDateTime}</td>
              </tr>
              <tr>
                <td>
                  <strong>Khoa</strong>
                </td>
                <td>{activities.faculty}</td>
              </tr>
              <tr>
                <td>
                  <strong>Người tham gia</strong>
                </td>
                <td>{activities.participant}</td>
              </tr>
              <tr>
                <td>
                  <strong>Điều</strong>
                </td>
                <td>{activities.article}</td>
              </tr>
            </tbody>
          </Table>

          <Table
            responsive="sm"
            striped
            bordered
            hover
            className="activity-details"
          >
            <thead>
              <tr align="middle">
                <th>#</th>
                <th>Loại Tham Gia</th>
                <th>Điểm</th>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {activities.activityParticipationTypes &&
                activities.activityParticipationTypes.length > 0 && (
                  <>
                    {activities.activityParticipationTypes.map(
                      (type, index) => (
                        <tr key={type.id} align="middle">
                          <td>{index + 1}</td>
                          <td>{type.participationType}</td>
                          <td>{type.point}</td>
                          <td>
                            <Button
                              onClick={(e) => activityRegistration(e, type.id)}
                              disabled={userActivity
                                .map((reg) => reg.acPartTypeId)
                                .includes(type.id)}
                              size="sm"
                            >
                              Đăng Ký
                            </Button>
                          </td>

                          <td>
                            <Button
                              variant="danger"
                              size="sm"
                              onClick={() => Show(type.id)}
                              disabled={reports.map((re) => re.activityPartType.id).includes(type.id)}
                            >
                              Báo Thiếu
                            </Button>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                )}
            </tbody>
          </Table>
          <MyModal
            Show={show}
            Close={Close}
            fileChange={fileChange}
            CreateReport={CreateReport}
            previewURL={previewURL}
            proof={proof}
            acPartTypedId={acPartType}
          />

          <div className="row">
            <div className="mb-2 d-flex" style={{ color: "#666" }}>
              <span>
                {activities.likes != 0 ? (
                  <div className="align-self-end me-2">
                    <small>
                      <img
                        style={{ marginTop: "-4px" }}
                        className=""
                        height="18"
                        role="presentation"
                        src="data:image/svg+xml,%3Csvg fill='none' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3E%3Cpath d='M16.0001 7.9996c0 4.418-3.5815 7.9996-7.9995 7.9996S.001 12.4176.001 7.9996 3.5825 0 8.0006 0C12.4186 0 16 3.5815 16 7.9996Z' fill='url(%23paint0_linear_15251_63610)'/%3E%3Cpath d='M16.0001 7.9996c0 4.418-3.5815 7.9996-7.9995 7.9996S.001 12.4176.001 7.9996 3.5825 0 8.0006 0C12.4186 0 16 3.5815 16 7.9996Z' fill='url(%23paint1_radial_15251_63610)'/%3E%3Cpath d='M16.0001 7.9996c0 4.418-3.5815 7.9996-7.9995 7.9996S.001 12.4176.001 7.9996 3.5825 0 8.0006 0C12.4186 0 16 3.5815 16 7.9996Z' fill='url(%23paint2_radial_15251_63610)' fill-opacity='.5'/%3E%3Cpath d='M7.3014 3.8662a.6974.6974 0 0 1 .6974-.6977c.6742 0 1.2207.5465 1.2207 1.2206v1.7464a.101.101 0 0 0 .101.101h1.7953c.992 0 1.7232.9273 1.4917 1.892l-.4572 1.9047a2.301 2.301 0 0 1-2.2374 1.764H6.9185a.5752.5752 0 0 1-.5752-.5752V7.7384c0-.4168.097-.8278.2834-1.2005l.2856-.5712a3.6878 3.6878 0 0 0 .3893-1.6509l-.0002-.4496ZM4.367 7a.767.767 0 0 0-.7669.767v3.2598a.767.767 0 0 0 .767.767h.767a.3835.3835 0 0 0 .3835-.3835V7.3835A.3835.3835 0 0 0 5.134 7h-.767Z' fill='%23fff'/%3E%3Cdefs%3E%3CradialGradient id='paint1_radial_15251_63610' cx='0' cy='0' r='1' gradientUnits='userSpaceOnUse' gradientTransform='rotate(90 .0005 8) scale(7.99958)'%3E%3Cstop offset='.5618' stop-color='%230866FF' stop-opacity='0'/%3E%3Cstop offset='1' stop-color='%230866FF' stop-opacity='.1'/%3E%3C/radialGradient%3E%3CradialGradient id='paint2_radial_15251_63610' cx='0' cy='0' r='1' gradientUnits='userSpaceOnUse' gradientTransform='rotate(45 -4.5257 10.9237) scale(10.1818)'%3E%3Cstop offset='.3143' stop-color='%2302ADFC'/%3E%3Cstop offset='1' stop-color='%2302ADFC' stop-opacity='0'/%3E%3C/radialGradient%3E%3ClinearGradient id='paint0_linear_15251_63610' x1='2.3989' y1='2.3999' x2='13.5983' y2='13.5993' gradientUnits='userSpaceOnUse'%3E%3Cstop stop-color='%2302ADFC'/%3E%3Cstop offset='.5' stop-color='%230866FF'/%3E%3Cstop offset='1' stop-color='%232B7EFF'/%3E%3C/linearGradient%3E%3C/defs%3E%3C/svg%3E"
                        width="18"
                      />
                    </small>
                    <small className="ms-1" style={{ fontSize: "16px" }}>
                      {activities.likes != 1 ? activities.likes : ""}
                    </small>
                  </div>
                ) : (
                  <></>
                )}
              </span>
              <span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="20"
                  height="20"
                  fill="currentColor"
                  className="bi bi-chat-fill"
                >
                  <path d="M8 15c4.418 0 8-3.134 8-7s-3.582-7-8-7-8 3.134-8 7c0 1.76.743 3.37 1.97 4.6-.097 1.016-.417 2.13-.771 2.966-.079.186.074.394.273.362 2.256-.37 3.597-.938 4.18-1.234A9 9 0 0 0 8 15" />
                </svg>
                {comments.length}
              </span>
            </div>
            <div className="">
              <button
                className={
                  activities.liked ? "btn btn-primary" : "btn border border-2"
                }
                onClick={likeOrUnlike}
              >
                {activities.liked ? (
                  <>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="20"
                      height="20"
                      fill="currentColor"
                      className="bi bi-hand-thumbs-up-fill"
                    >
                      <path d="M6.956 1.745C7.021.81 7.908.087 8.864.325l.261.066c.463.116.874.456 1.012.965.22.816.533 2.511.062 4.51a10 10 0 0 1 .443-.051c.713-.065 1.669-.072 2.516.21.518.173.994.681 1.2 1.273.184.532.16 1.162-.234 1.733q.086.18.138.363c.077.27.113.567.113.856s-.036.586-.113.856c-.039.135-.09.273-.16.404.169.387.107.819-.003 1.148a3.2 3.2 0 0 1-.488.901c.054.152.076.312.076.465 0 .305-.089.625-.253.912C13.1 15.522 12.437 16 11.5 16H8c-.605 0-1.07-.081-1.466-.218a4.8 4.8 0 0 1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 14.464 2 13.846 2 13V9c0-.85.685-1.432 1.357-1.615.849-.232 1.574-.787 2.132-1.41.56-.627.914-1.28 1.039-1.639.199-.575.356-1.539.428-2.59z" />
                    </svg>{" "}
                    Đã thích
                  </>
                ) : (
                  <>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="20"
                      height="20"
                      fill="currentColor"
                      className="bi bi-hand-thumbs-up"
                    >
                      <path d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2 2 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a10 10 0 0 0-.443.05 9.4 9.4 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a9 9 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.2 2.2 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.9.9 0 0 1-.121.416c-.165.288-.503.56-1.066.56z" />
                    </svg>{" "}
                    Thích
                  </>
                )}
              </button>
            </div>

            <div className="mt-4">
              <h3>Bình luận</h3>
              <CommentList
                comments={comments}
                handleDelete={deleteComment}
                handleUpdate={updateComment}
              />
            </div>
            <div className="d-flex mb-3">
              <Image
                src={user.avatar}
                width="30"
                height="30"
                className="me-2"
                roundedCircle
              />
              <InputForm
                // onChange={setComment}
                handleSubmit={addComment}
                placeholder={
                  "Bình luận với vai trò " +
                  user.lastName +
                  " " +
                  user.firstName
                }
              />
            </div>
          </div>
        </>
      )}
    </>
  );
};

export default ActivityDetails;
