import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { authApi, endpoints } from "../../configs/APIs";
import { Button, Form, Image, Table } from "react-bootstrap";
import { MyUserContext } from "../../configs/Contexts";

const ActivityDetails = () => {
  const [activity, setActivity] = useState({});
  const { activityId } = useParams();
  const [comment, setComment] = useState({});
  const user = useContext(MyUserContext);

  const loadActivity = async () => {
    try {
      let res = await authApi().get(endpoints["activity-details"](activityId));
      setActivity(res.data);
      console.log(res.data);
    } catch (ex) {
      console.error(ex);
    }
  };

  const Change = (e) => {
    e.preventDefault();
    setComment((current) => {
      return { ...current, content: e.target.value };
    });
  };

  const addComment = async (e) => {
    e.preventDefault();
    console.log(comment);
    try {
      let res = await authApi().post(
        endpoints["activity-add-comment"](activityId),
        comment
      );
      if (res.status === 201) {
        let resData = res.data;
        let c = document.getElementById("commentId");
        let li = document.createElement("li");
        li.className = "mb-3";
        li.id = `${resData.id}`;
        li.innerHTML = `
        <img src="${
          resData.avatar
        }" width="40" height="40" class="rounded-circle" /> ${" "}
        <strong>${resData.name}</strong>: ${resData.content} - ${" "}
        ${new Date(resData.createdAt).toLocaleDateString("vi-VN")}
      `;
        c.appendChild(li);
        setComment({ content: "" });
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  useEffect(() => {
    loadActivity();
  }, []);

  return (
    <>
      <h1 className="text-center mt-3">Chi Tiết Hoạt Động</h1>
      <Table striped bordered hover className="activity-details">
        <tbody>
          <tr>
            <td>
              <strong>Tên Hoạt Động</strong>
            </td>
            <td>{activity.name}</td>
          </tr>
          <tr>
            <td>
              <strong>Địa điểm</strong>
            </td>
            <td>{activity.location}</td>
          </tr>
          <tr>
            <td>
              <strong>Thời gian bắt đầu</strong>
            </td>
            <td>{activity.startDateTime}</td>
          </tr>
          <tr>
            <td>
              <strong>Thời gian kết thúc</strong>
            </td>
            <td>{activity.endDateTime}</td>
          </tr>
          <tr>
            <td>
              <strong>Khoa</strong>
            </td>
            <td>{activity.faculty}</td>
          </tr>
          <tr>
            <td>
              <strong>Người tham gia</strong>
            </td>
            <td>{activity.participant}</td>
          </tr>
          {activity.activityParticipationTypes && (
            <tr>
              <td>
                <strong>Loại tham gia</strong>
              </td>
              <td>
                <ul>
                  {activity.activityParticipationTypes.map((type) => (
                    <li key={type.id}>
                      {type.participationType} - {type.point} điểm
                    </li>
                  ))}
                </ul>
              </td>
            </tr>
          )}
          <tr>
            <td>
              <strong>Điều khoản</strong>
            </td>
            <td>{activity.article}</td>
          </tr>
        </tbody>
      </Table>

      <div className="mt-4">
        <h3>Bình luận</h3>
        <ul id="commentId" className="mb-3">
          {activity.comments &&
            activity.comments.length > 0 &&
            activity.comments.map((comment) => (
              <li id={comment.id} className="mb-3">
                <Image
                  src={comment.avatar}
                  width="40"
                  height="40"
                  roundedCircle
                />{" "}
                <strong>{comment.name}</strong>: {comment.content} -{" "}
                {new Date(comment.createdAt).toLocaleDateString("vi-VN")}
              </li>
            ))}
        </ul>
      </div>

      <Form onSubmit={addComment}>
        <Form.Group>
          <Form.Control
            onChange={(e) => Change(e)}
            value={comment.content}
            as="textarea"
            className="mb-3"
          ></Form.Control>
        </Form.Group>
        <Button type="submit" width="40" className="me-auto mb-3">
          Thêm Bình Luận
        </Button>
      </Form>
    </>
  );
};

export default ActivityDetails;
