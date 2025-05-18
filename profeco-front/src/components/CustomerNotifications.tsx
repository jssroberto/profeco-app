import { useEffect, useState, useCallback, useMemo } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import Button from "./ui/Button";

interface Notification {
  id: string;
  message: string;
  createdAt: string;
  type: string;
  link?: string;
  read: boolean;
}

const NOTIFICATION_API_BASE = "http://localhost:8080/api/v1/notifications";

const notificationTypeConfig: Record<string, { bg: string; text: string; border: string }> = {
  NUEVA_OFERTA: {
    bg: "bg-[#681837]",
    text: "text-white",
    border: "border-[#681837]"
  },
  ALERTA: {
    bg: "bg-[#B91C1C]",
    text: "text-white",
    border: "border-[#B91C1C]"
  },
  INFO: {
    bg: "bg-[#1D4ED8]",
    text: "text-white",
    border: "border-[#1D4ED8]"
  },
  DEFAULT: {
    bg: "bg-[#374151]",
    text: "text-white",
    border: "border-[#374151]"
  }
};

const CustomerNotifications = () => {
  const { token } = useAuth();
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [visible, setVisible] = useState<Record<string, boolean>>({});
  const [showPanel, setShowPanel] = useState(false);
  const [allNotifications, setAllNotifications] = useState<Notification[]>([]);
  const navigate = useNavigate();

  const apiHeaders = useMemo(() => ({
    headers: { Authorization: `Bearer ${token}` }
  }), [token]);

  const fetchNotifications = useCallback(async () => {
    if (!token) return;
    
    try {
      const res = await axios.get(
        `http://localhost:8080/api/me/notifications/active`,
        apiHeaders
      );
      
      setNotifications(res.data);
      // Mostrar todas las notificaciones por defecto
      const visibilityMap: Record<string, boolean> = {};
      res.data.forEach((n: Notification) => (visibilityMap[n.id] = true));
      setVisible(visibilityMap);
    } catch (error) {
      console.error("Error fetching notifications:", error);
    }
  }, [token, apiHeaders]);

  // Update the endpoint for fetching all notifications
  const fetchAllNotifications = useCallback(async () => {
    if (!token) return;

    try {
      const res = await axios.get(
        `http://localhost:8080/api/me/notifications`,
        apiHeaders
      );
      setAllNotifications(res.data);
    } catch (error) {
      console.error("Error fetching all notifications:", error);
    }
  }, [token, apiHeaders]);

  useEffect(() => {
    if (!token) return;
    
    fetchNotifications();
    const interval = setInterval(fetchNotifications, 10000);
    return () => clearInterval(interval);
  }, [fetchNotifications, token]);

  const markAsRead = useCallback(async (id: string) => {
    try {
      await axios.patch(
        `${NOTIFICATION_API_BASE}/${id}/read`,
        {},
        apiHeaders
      );
      fetchNotifications();
    } catch (error) {
      console.error("Error marking notification as read:", error);
    }
  }, [apiHeaders, fetchNotifications]);

  const handleClose = useCallback(async (id: string) => {
    setVisible(prev => ({ ...prev, [id]: false }));
    await markAsRead(id);
  }, [markAsRead]);

  const handleLink = useCallback(async (link?: string, id?: string) => {
    if (id) {
      await markAsRead(id);
    }

    if (!link) return;

    // Links internos
    if (link.startsWith("/")) {
      const match = link.match(/\/([\w-]+)\/products\/([\w-]+)/);
      if (match) {
        const [, storeId, productId] = match;
        navigate(`/negocios/${storeId}/productos/${productId}`);
        return;
      }
      navigate(link);
    } 
    // Links externos (con validación básica)
    else if (/^https?:\/\//.test(link)) {
      window.open(link, "_blank", "noopener,noreferrer");
    }
  }, [markAsRead, navigate]);

  const notificationCount = useMemo(() => notifications.length, [notifications]);

  const renderNotification = (n: Notification) => {
    const config = notificationTypeConfig[n.type] || notificationTypeConfig.DEFAULT;
    
    return (
      <div
        key={n.id}
        className={`border shadow-xl rounded-xl p-4 mb-4 min-w-[320px] max-w-xs flex flex-col gap-2 animate-fade-in ${config.bg} ${config.text} ${config.border}`}
        style={{ boxShadow: "0 6px 24px rgba(0,0,0,0.18)" }}
      >
        <div className="flex justify-between items-start">
          <div className="flex-1 pr-2">
            <div className="font-bold mb-1 text-base capitalize">
              {n.type.replace(/_/g, " ").toLowerCase()}
            </div>
            <div className="mb-2 text-sm">{n.message}</div>
            {n.link && (
              <Button
                onClick={() => handleLink(n.link, n.id)}
                className="text-xs font-semibold px-3 py-1 rounded-full bg-white hover:bg-opacity-90 shadow transition"
                style={{ color: config.bg.replace("bg-", "text-") }}
              >
                Ver más
              </Button>
            )}
            <div className="text-xs opacity-80 mt-2">
              {new Date(n.createdAt).toLocaleString()}
            </div>
          </div>
          <button
            onClick={() => handleClose(n.id)}
            className="ml-2 hover:opacity-70 text-lg font-bold focus:outline-none"
            aria-label="Cerrar notificación"
          >
            ×
          </button>
        </div>
      </div>
    );
  };

  return (
    <>
      <div className="fixed bottom-6 right-6 z-[9999]">
        {notifications
          .filter(n => visible[n.id])
          .map(renderNotification)}
          
        <button
          onClick={() => {
            setShowPanel(v => !v);
            if (!showPanel) fetchAllNotifications();
          }}
          className="fixed bottom-8 right-8 bg-[#681837] text-white rounded-full w-14 h-14 flex items-center justify-center shadow-lg border-2 border-white hover:bg-[#4a1025] transition z-50"
          style={{ fontSize: 28 }}
          aria-label={showPanel ? "Ocultar notificaciones" : "Mostrar notificaciones"}
        >
          <span className="relative">
            <svg width="28" height="28" fill="none" viewBox="0 0 24 24">
              <path
                fill="currentColor"
                d="M12 2a6 6 0 0 0-6 6v3.278c0 .456-.186.893-.516 1.212A3.003 3.003 0 0 0 4 15v1h16v-1a3.003 3.003 0 0 0-1.484-2.51A1.75 1.75 0 0 1 18 11.278V8a6 6 0 0 0-6-6Zm0 20a2.5 2.5 0 0 1-2.45-2h4.9A2.5 2.5 0 0 1 12 22Z"
              />
            </svg>
            {notificationCount > 0 && (
              <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full px-2 py-0.5 font-bold">
                {notificationCount}
              </span>
            )}
          </span>
        </button>
      </div>

      {/* Panel de notificaciones */}
      {showPanel && (
        <div className="fixed top-0 right-0 w-full max-w-md h-full bg-white shadow-2xl z-[9999] flex flex-col border-l border-gray-200 animate-slide-in">
          <div className="flex items-center justify-between p-4 border-b border-gray-200 bg-[#681837]">
            <span className="text-lg font-bold text-white">
              Tus notificaciones
            </span>
            <button
              onClick={() => setShowPanel(false)}
              className="text-white text-2xl font-bold hover:opacity-70 focus:outline-none"
              aria-label="Cerrar panel de notificaciones"
            >
              ×
            </button>
          </div>
          <div className="flex-1 overflow-y-auto p-4 space-y-4">
            {allNotifications.length === 0 ? (
              <div className="text-gray-500 text-center mt-8">
                No tienes notificaciones.
              </div>
            ) : (
              allNotifications.map(n => (
                <div
                  key={n.id}
                  className={`border rounded-lg p-3 flex flex-col gap-1 ${
                    n.read
                      ? "bg-gray-100 border-gray-200"
                      : "bg-[#f3e6ef] border-[#681837]"
                  }`}
                >
                  <div className="flex justify-between items-center">
                    <span
                      className={`font-semibold ${
                        n.read ? "text-gray-500" : "text-[#681837]"
                      }`}
                    >
                      {n.type.replace(/_/g, " ").toLowerCase()}
                    </span>
                    {!n.read && (
                      <span className="bg-[#681837] text-white text-xs rounded-full px-2 py-0.5 ml-2">
                        No leída
                      </span>
                    )}
                  </div>
                  <div className="text-sm text-gray-800">{n.message}</div>
                  {n.link && (
                    <Button
                      onClick={() => handleLink(n.link, n.id)}
                      className="text-xs font-semibold px-2 py-1 rounded bg-[#681837] text-white hover:bg-[#4a1025] mt-1"
                    >
                      Ver más
                    </Button>
                  )}
                  <div className="text-xs text-gray-400 mt-1">
                    {new Date(n.createdAt).toLocaleString()}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      )}
    </>
  );
};

export default CustomerNotifications;