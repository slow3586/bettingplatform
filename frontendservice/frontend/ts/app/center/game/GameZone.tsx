import React, {useState} from "react";
import 'chartjs-adapter-date-fns';
import {Chart} from "primereact/chart";
import {useStompClient, useSubscription} from "react-stomp-hooks";
import {format} from "date-fns";

export function GameZone() {
    const [chartData, setChartData] = useState([]);
    const stompClient = useStompClient();

    if (chartData.length == 0 && stompClient != null) {
        stompClient.publish({destination: "/app/price_latest", body: ""});
    }

    useSubscription("/user/topic/feed",
        (message) => {
            console.log(message);
        });

    useSubscription("/topic/price",
        (message) => {
            console.log(message);
            setChartData([
                ...chartData,
                {
                    x: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
                    y: message.body
                }]);
        });

    return (
        <div className="bp-betting-chart-container">
            <Chart
                className="bp-betting-chart"
                type="line"
                data={{
                    datasets: [{
                        data: chartData
                    }],
                }}
                options={{
                    scales: {
                        x: {
                            type: 'time',
                            time: {
                                unit: 'minute'
                            }
                        }
                    },
                    responsive: true,
                    maintainAspectRatio: false
                }}/>
        </div>
    )
}