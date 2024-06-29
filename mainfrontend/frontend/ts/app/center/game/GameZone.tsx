import React, {useState} from "react";
import 'chartjs-adapter-date-fns';
import {Chart} from "primereact/chart";
import {useSubscription} from "react-stomp-hooks";
import {format} from "date-fns";
import {useQuery} from "react-query";
import axios from "axios";

export function GameZone() {
    const [chartData, setChartData] = useState([]);

    const priceQuery = useQuery(
        'price',
        async () => await axios.get('/price'),
        {
            enabled: false
        });
    const price = priceQuery?.data?.data;

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
                        data: [...price, chartData]
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