"use client"; // This is a client component

import React, { useState } from "react";
import { Button, List, Card, Spin, Typography, Row, Col, Tag } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import axios from 'axios';
import styles from "./page.module.css";

const apiList = [
  { name: "Get User Info", method: "GET", endpoint: "/api/user", hasParams: true },
  { name: "Fetch Orders", method: "POST", endpoint: "/api/orders", hasParams: true },
  { name: "Check Status", method: "GET", endpoint: "/api/status", hasParams: false }
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

  const handleApiCall = async (endpoint, method, hasParams) => {
    setLoading(true);
    const startTime = performance.now();

    let url = `http://localhost:8082${endpoint}`;
    const options = {
      method,
      headers: { "Content-Type": "application/json" },
      withCredentials: true
    };

    if (hasParams) {
      const userId = prompt("Please enter User ID:");
      if (!userId) {
        setResult({ error: "User ID is required." });
        setLoading(false);
        return;
      }

      if (method === "GET") {
        url += `?userId=${userId}`;
      } else if (method === "POST") {
        options.data = { userId };
      }
    }

    try {
      const response = await axios(url, options);
      const endTime = performance.now();

      setResult(response.data);
      setExecutionTime((endTime - startTime).toFixed(2) + " ms");
    } catch (error) {
      setResult({ error: "API call failed" });
      setExecutionTime(null);
    }

    setLoading(false);
  };

  return (
    <div className={styles.wrapper}>
      <div className={styles.banner}>Alias RPC Framework - Demo Page</div>
      <div className={styles.container}>

        {/* 左侧 API 列表 */}
        <div className={styles.sidebar}>
          <Typography.Title level={3}>API List</Typography.Title>
          <List
            bordered
            dataSource={apiList}
            renderItem={(item) => (
              <List.Item>
                <Row gutter={16} align="middle">
                  <Col>
                    <Tag color={getMethodColor(item.method)}>{item.method}</Tag>
                  </Col>
                  <Col>
                    <Typography.Text strong>{item.name}</Typography.Text>
                  </Col>
                  <Col>
                    <Typography.Text>{item.endpoint}</Typography.Text>
                  </Col>
                  <Col>
                    <Button
                      onClick={() => handleApiCall(item.endpoint, item.method, item.hasParams)}
                      color="primary"
                      variant="filled"
                    >
                      Call API
                    </Button>
                  </Col>
                </Row>
              </List.Item>
            )}
          />
        </div>

        {/* 右侧 结果展示 */}
        <div className={styles.content}>
          <Typography.Title level={3}>Result</Typography.Title>
          {loading ? (
            <Spin indicator={<LoadingOutlined spin />} />
          ) : result ? (
            <Card>
              <pre>{JSON.stringify(result, null, 2)}</pre>
              {executionTime && (
                <Typography.Text type="secondary">
                  Execution Time: {executionTime}
                </Typography.Text>
              )}
            </Card>
          ) : (
            <Typography.Text>No data yet. Select an API to call.</Typography.Text>
          )}
        </div>
      </div>
    </div>
  );
}