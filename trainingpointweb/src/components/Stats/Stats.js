import { useContext, useEffect, useState } from "react";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import { MyUserContext } from "../../configs/Contexts";
import { useLocation, useNavigate } from "react-router-dom";
import { Chart, defaults } from "chart.js/auto";
import { Bar, Line } from "react-chartjs-2";
import { DropdownButton, Dropdown } from "react-bootstrap";

const Stats = () => {
  const [activites, setActivites] = useState([]);
  const [data, setData] = useState(null);
  const { user } = useContext(MyUserContext);
  const nav = useNavigate();
  const location = useLocation();

  const Load = async () => {
    try {
      let res = await authApi().get(endpoints["user-registration"]);
      let article = await APIs.get(endpoints["article-list"]);

      if (res.status === 200 && article.status === 200) {
        setActivites(res.data);
        const articleNames = article.data.map((article) => article.name);
        const confirmedPoints = articleNames.map((articleName) => {
          return res.data.reduce((acc, activity) => {
            if (
              activity.activity.article === articleName &&
              activity.participated
            ) {
              return acc + activity.point;
            }
            return acc;
          }, 0);
        });
        const registeredPoints = articleNames.map((articleName) => {
          return res.data.reduce((acc, activity) => {
            if (
              activity.activity.article === articleName &&
              !activity.participated
            ) {
              return acc + activity.point;
            }
            return acc;
          }, 0);
        });

        setData({
          labels: article.data.map((r) => r.name),
          datasets: [
            {
              label: "Điểm tối đa",
              data: [25, 20, 20, 25, 10, 10],
              backgroundColor: "rgba(255, 99, 132, 0.2)",
              borderColor: "rgba(255, 99, 132, 1)",
              borderWidth: 1,
            },
            {
              label: "Điểm xác nhận",
              data: confirmedPoints,
              backgroundColor: "rgba(54, 162, 235, 0.2)",
              borderColor: "rgba(54, 162, 235, 1)",
              borderWidth: 1,
            },
            {
              label: "Điểm đăng ký",
              data: registeredPoints,
              backgroundColor: "rgba(75, 192, 192, 0.2)",
              borderColor: "rgba(75, 192, 192, 1)",
              borderWidth: 1,
            },
          ],
        });
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  defaults.maintainAspectRatio = true;
  defaults.responsive = true;

  useEffect(() => {
    if (user == null) {
      nav("/login", { state: { from: location } });
    }
    Load();
  }, [user]);

  return (
    <>
      {user != null && (
        <>
          <DropdownButton id="dropdown-basic-button" title="Dropdown button">
            <Dropdown.Item href="#/action-1">Action</Dropdown.Item>
            <Dropdown.Item href="#/action-2">Another action</Dropdown.Item>
            <Dropdown.Item href="#/action-3">Something else</Dropdown.Item>
          </DropdownButton>
          {data && (
            <>
              <h1 className="text-center m-3">Thống Kê Điểm Rèn Luyện</h1>
              <Bar
                data={data}
                options={{
                  legend: { display: false },
                  title: {
                    display: true,
                    text: "Predicted world population (millions) in 2050",
                  },
                }}
              />
            </>
          )}
        </>
      )}
    </>
  );
};

export default Stats;
