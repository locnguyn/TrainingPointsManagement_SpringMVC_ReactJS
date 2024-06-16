import { useContext, useEffect, useState } from "react";
import {
  Button,
  Table,
} from "react-bootstrap";
import "./styles.css";
import { authApi, endpoints } from "../../configs/APIs";
import { useLocation, useNavigate } from "react-router-dom";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { FaMinusCircle } from "react-icons/fa";
import toast from "react-hot-toast";
const Activity = () => {
  const user = useContext(MyUserContext);
  const [activites, setActivites] = useState(user ? user.userRegistration: []);
  const nav = useNavigate();
  const location = useLocation();
  const dispatch = useContext(MyDispatcherContext);

  const delRegistration = async (e, registrationId) =>{
    e.preventDefault();

    try{
      let res = await toast.promise(authApi().delete(endpoints['registration-delete'](registrationId)),{
        loading: "Loading", success: "Xóa thành công", error: "Thất bại"
      });

      if(res.status === 200){
        setActivites((preActivities) =>
          preActivities.filter((a) => a.id != registrationId)
        );
      }
    }catch(ex){
      console.error(ex);
    }
  }

  useEffect(() =>{
    if(user !== null){
      dispatch({
        type: "update_user",
        payload:{
          resData: user.userInfo,
          regData: [...activites]
        }
      });
    }
  }, [activites]);
  
  useEffect(() => {
    if (user === null){
      nav('/login', {state: {from: location}});
    }
  }, [user?.userInfo, nav, location]);
  return (
    <>
      {user !== null &&<>
          <h1 className="text-center mt-3">Hoạt Động Tham Gia</h1>
          <Table responsive="sm" striped bordered hover className="p-3a m-3">
        <thead>
          <tr>
            <th>#</th>
            <th>Tên Hoạt Động</th>
            <th>Địa Điểm</th>
            <th>Điều</th>
            <th>Đối tượng</th>
            <th>Hình thức</th>
            <th>Điểm</th>
            <th>Khoa</th>
            <th>Ngày giờ bắt đầu</th>
            <th>Ngày giờ kết thúc</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {activites.map((a, index) => (
            <tr key={a.id}>
              <td>{index + 1}</td>
              <td>{a.activity.name}</td>
              <td>{a.activity.location}</td>
              <td>{a.activity.article}</td>
              <td>{a.activity.participant}</td>
              <td>{a.participationTypeName}</td>
              <td>{a.point}</td>
              <td>{a.activity.faculty}</td>
              <td>{a.activity.startDateTime}</td>
              <td>{a.activity.endDateTime}</td>
              <td align="middle"><Button variant="danger" onClick={(e) => delRegistration(e, a.id)} responsive size="sm">Xóa</Button></td>
            </tr>
          ))}
        </tbody>
      </Table>
        </>
      }
    </>
  );
};

export default Activity;
