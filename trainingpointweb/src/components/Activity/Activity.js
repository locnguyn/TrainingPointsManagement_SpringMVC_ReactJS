import axios from "axios";
import { useContext, useEffect, useState } from "react";
import {
  Button,
  Container,
  Nav,
  NavDropdown,
  Navbar,
  Table,
} from "react-bootstrap";
import "./styles.css";
import { authApi, endpoints } from "../../configs/APIs";
import { useLocation, useNavigate } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
const Activity = () => {
  const [activites, setActivites] = useState([]);
  const nav = useNavigate();
  const location = useLocation();
  const user = useContext(MyUserContext);
  const loadActivity = async () => {
      let res = await authApi().get(endpoints['activites']);
      setActivites(res.data);
  };

  useEffect(() => {
    if (user === null){
      nav('/login', {state: {from: location}});
    }else{
      loadActivity();
    }
  }, [user, nav, location]);
  return (
    <>
      {user !== null &&<>
          <h1 className="text-center mt-3">Hoạt Động</h1>
          <Table striped bordered hover className="p-3a m-3">
        <thead>
          <tr>
            <th>#</th>
            <th>Tên Hoạt Động</th>
            <th colSpan={2}>Địa Điểm</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {activites.map((a, index) => (
            <tr key={a.id}>
              <td>{index + 1}</td>
              <td colSpan={2}>{a.name}</td>
              <td >{a.location}</td>
              <td><Button variant="success">+</Button></td>
            </tr>
          ))}
          
        </tbody>
      </Table>
            {/* <ul>
                {activites.map(a => <li>
                    <h1>{a.id} - {a.name} - {a.location}</h1>
                    
                </li>)}
            </ul> */}
        </>
      }
    </>
  );
};

export default Activity;
