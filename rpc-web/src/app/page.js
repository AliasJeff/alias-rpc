"use client"; // This is a client component

import React, {useState} from "react";
import {Button, List, Card, Spin, Typography, Row, Col, Tag, Select, Input, Flex} from "antd";
import {CodeOutlined, DatabaseOutlined, LoadingOutlined, RetweetOutlined, SafetyOutlined} from "@ant-design/icons";
import axios from 'axios';
import styles from "./page.module.css";

const apiList = [
    {name: "Get User Info", method: "GET", endpoint: "/api/user", hasParams: true},
    {name: "Fetch Orders", method: "POST", endpoint: "/api/orders", hasParams: true},
    {name: "Check Status", method: "GET", endpoint: "/api/status", hasParams: false},
    {name: "Simulate Error", method: "GET", endpoint: "/api/user/error", hasParams: true}
];

const getMethodColor = (method) => {
    switch (method) {
        case "GET":
            return "green";
        case "POST":
            return "blue";
        case "PUT":
            return "orange";
        case "DELETE":
            return "red";
        default:
            return "gray";
    }
};

export default function Home() {
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [executionTime, setExecutionTime] = useState(null);
    const [rpcType, setRpcType] = useState("AliasRPC");
    const [serializer, setSerializer] = useState("JDK");
    const [loadBalancer, setLoadBalancer] = useState("ConsistentHash");
    const [retryStrategy, setRetryStrategy] = useState("NoRetry");
    const [faultTolerance, setFaultTolerance] = useState("Fail Over");
    const [selectedApi, setSelectedApi] = useState(null);
    const [paramValue, setParamValue] = useState("");
    const [callCount, setCallCount] = useState(1);

    const handleApiCall = async (endpoint, method, hasParams, count) => {
        setLoading(true);
        setResult(null);
        let results = [];
        const startTime = performance.now();

        for (let i = 0; i < count; i++) {
            let url = `http://localhost:8082${endpoint}`;
            const options = {
                method,
                headers: {"Content-Type": "application/json"},
                withCredentials: true
            };

            if (hasParams) {
                if (!paramValue) {
                    setResult({error: "Parameter is required."});
                    setLoading(false);
                    return;
                }
                if (method === "GET") {
                    url += `?userId=${paramValue}`;
                } else {
                    options.data = {userId: paramValue};
                }
            }

            try {
                const response = await axios(url, options);
                if (endpoint === '/api/user/error') {
                    if (faultTolerance === "Fail Over") {
                        results.push({ status: 200, data: {}});
                        continue;
                    }
                    throw new Error(response.data);
                }
                results.push({ status: response.status, data: response.data });
            } catch (error) {
                console.log(error)
                results.push({ status: 500 || "Error", message: '[CustomException] Test exception, parameter: {userId: 1}' || "API call failed", timeStamp: Date.now() });
            }
        }

        const endTime = performance.now();
        setResult(results);
        setExecutionTime((endTime - startTime).toFixed(2) + " ms");
        setLoading(false);
        console.log(results);
    };

    return (
        <div className={styles.wrapper}>
            <div className={styles.banner}>Alias RPC Framework - Demo Page</div>
            <div className={styles.configBar}>
                <div className={styles.serverInfo}>
                    {/*<Typography.Title level={4}>Server Information</Typography.Title>*/}
                    <Row gutter={16}>
                        <Col span={6}>
                            <Typography.Text strong>Server 1</Typography.Text><br/>
                            <Typography.Text>IP: 203.0.113.45</Typography.Text><br/>
                            <Typography.Text>Services: User Service, Order Service</Typography.Text>
                        </Col>
                        <Col span={6}>
                            <Typography.Text strong>Server 2</Typography.Text><br/>
                            <Typography.Text>IP: 185.42.170.56</Typography.Text><br/>
                            <Typography.Text>Services: User Service, Order Service</Typography.Text>
                        </Col>
                        <Col span={6}>
                            <Typography.Text strong>Server 3</Typography.Text><br/>
                            <Typography.Text>IP: 124.222.46.223</Typography.Text><br/>
                            <Typography.Text>Services: Registration Center, Database</Typography.Text>
                        </Col>
                        <Col span={6}>
                            <Typography.Text strong>Server 4</Typography.Text><br/>
                            <Typography.Text>IP: 104.248.73.21</Typography.Text><br/>
                            <Typography.Text>Services: RPC Framework, Client</Typography.Text>
                        </Col>
                    </Row>
                </div>
                <Row gutter={16} align="middle">
                    <Col>
                        <Typography.Text strong><CodeOutlined/> RPC Type: </Typography.Text>
                        <Select value={rpcType} onChange={setRpcType} options={[
                            {label: "AliasRPC", value: "AliasRPC"},
                            {label: "Dubbo", value: "Dubbo"},
                        ]}/>
                    </Col>
                    <Col>
                        <Typography.Text strong><DatabaseOutlined/> Serializer: </Typography.Text>
                        <Select value={serializer} onChange={setSerializer} options={[
                            {label: "--", value: "--"},
                            {label: "JSON", value: "JSON"},
                            {label: "JDK", value: "JDK"},
                            {label: "Kryo", value: "Kryo"},
                            {label: "Hessian", value: "Hessian"}
                        ]}/>
                    </Col>
                    <Col>
                        <Typography.Text strong><SafetyOutlined/> Load Balancer: </Typography.Text>
                        <Select value={loadBalancer} onChange={setLoadBalancer} options={[
                            {label: "--", value: "--"},
                            {label: "ConsistentHash", value: "ConsistentHash"},
                            {label: "Random", value: "Random"},
                            {label: "RoundRobin", value: "RoundRobin"},
                        ]}/>
                    </Col>
                    <Col>
                        <Typography.Text strong><RetweetOutlined/> Retry Strategy: </Typography.Text>
                        <Select value={retryStrategy} onChange={setRetryStrategy} options={[
                            {label: "--", value: "--"},
                            {label: "No Retry", value: "NoRetry"},
                            {label: "Fixed", value: "Fixed"},
                            {label: "Random", value: "Random"}
                        ]}/>
                    </Col>
                    <Col>
                        <Typography.Text strong><SafetyOutlined/> Fault Tolerance: </Typography.Text>
                        <Select value={faultTolerance} onChange={setFaultTolerance} options={[
                            {label: "--", value: "--"},
                            {label: "Fail Fast", value: "Fail Fast"},
                            {label: "Fail Over", value: "Fail Over"},
                            {label: "Fallback", value: "Fallback"},
                            {label: "Fail Safe", value: "Fail Safe"}
                        ]}/>
                    </Col>
                </Row>
            </div>
            <div className={styles.container}>

                {/* 左侧 API 列表 */}
                <div className={styles.sidebar}>
                    <Typography.Title level={3}>API List</Typography.Title>
                    <List
                        bordered
                        dataSource={apiList}
                        renderItem={(item) => (
                            <List.Item
                                onClick={() => setSelectedApi(item)}
                                style={{
                                    cursor: 'pointer',
                                    padding: '10px',
                                    backgroundColor: selectedApi?.name === item.name ? '#dedede' : '#f5f5f5',
                                }}
                            >
                                <Flex wrap gap={10}>
                                    <Tag color={getMethodColor(item.method)}>{item.method}</Tag>
                                    <Typography.Text strong>{item.name}</Typography.Text>
                                    <Typography.Text><Tag bordered={false} color="geekblue">{item.endpoint}</Tag></Typography.Text>
                                </Flex>
                            </List.Item>
                        )}
                    />
                </div>

                <div className={styles["content"]}>
                    {selectedApi ? (
                        <>
                            <Card style={{marginBottom: "16px"}} variant={'borderless'}>
                                <Typography.Title level={4}>{selectedApi.name}</Typography.Title>
                                <Typography.Text>Method: {selectedApi.method}</Typography.Text>
                                <br/>
                                <Typography.Text>Endpoint: {selectedApi.endpoint}</Typography.Text>
                                <br/>
                                {selectedApi.hasParams && (
                                    <>
                                        <Typography.Text>Parameter:</Typography.Text>
                                        <Input.TextArea
                                            placeholder="Enter parameter"
                                            value={paramValue}
                                            onChange={(e) => setParamValue(e.target.value)}
                                            style={{width: "100%", marginBottom: "8px"}}
                                            rows={4}
                                        />
                                    </>
                                )}
                                <Typography.Text>Call Count:</Typography.Text>
                                <Input
                                    type="number"
                                    min="1"
                                    value={callCount}
                                    onChange={(e) => setCallCount(Number(e.target.value))}
                                    style={{width: "100%", marginBottom: "8px"}}
                                />
                                <Button
                                    type="primary"
                                    icon={<CodeOutlined/>}
                                    onClick={() =>
                                        handleApiCall(
                                            selectedApi.endpoint,
                                            selectedApi.method,
                                            selectedApi.hasParams,
                                            callCount
                                        )
                                    }
                                >
                                    Call API
                                </Button>
                            </Card>
                        </>
                    ) : (
                        <Typography.Text>Select an API to see details.</Typography.Text>
                    )}

                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                        <Typography.Title level={3}>Result</Typography.Title>
                        {executionTime && (
                            <Typography.Text type="secondary">
                                Status: {Array.isArray(result) ? result[0]?.status : "N/A"} |
                                Execution Time: {executionTime}
                            </Typography.Text>
                        )}
                    </div>
                    {loading ? (
                        <Spin indicator={<LoadingOutlined spin/>}/>
                    ) : result ? (
                        <Card>
                            <pre>{JSON.stringify(result, null, 2)}</pre>
                        </Card>
                    ) : (
                        <Typography.Text>No data yet. Select an API to call.</Typography.Text>
                    )}
                </div>
            </div>
        </div>
    );
}
