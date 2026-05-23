import { useState, useEffect } from 'react';
import { LayoutDashboard, Monitor, Ticket, Users, Activity, HardDrive, Smartphone, Server } from 'lucide-react';
import Dashboard from './components/Dashboard';
import Assets from './components/Assets';
import Tickets from './components/Tickets';

// Using environment variable for API URL (defaults to localhost for development)
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
const AUTH_HEADER = 'Basic ' + btoa('admin:admin123');

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [assets, setAssets] = useState([]);
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [assetsRes, ticketsRes] = await Promise.all([
          fetch(`${API_URL}/assets`, { headers: { 'Authorization': AUTH_HEADER } }),
          fetch(`${API_URL}/tickets`, { headers: { 'Authorization': AUTH_HEADER } })
        ]);
        
        const assetsData = await assetsRes.json();
        const ticketsData = await ticketsRes.json();
        
        setAssets(assetsData);
        setTickets(ticketsData);
      } catch (error) {
        console.error("Failed to fetch data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="logo">ECLearnix IT</div>
        <nav className="nav-links">
          <div 
            className={`nav-link ${activeTab === 'dashboard' ? 'active' : ''}`}
            onClick={() => setActiveTab('dashboard')}
          >
            <LayoutDashboard size={20} /> Dashboard
          </div>
          <div 
            className={`nav-link ${activeTab === 'assets' ? 'active' : ''}`}
            onClick={() => setActiveTab('assets')}
          >
            <Monitor size={20} /> Assets
          </div>
          <div 
            className={`nav-link ${activeTab === 'tickets' ? 'active' : ''}`}
            onClick={() => setActiveTab('tickets')}
          >
            <Ticket size={20} /> Tickets
          </div>
        </nav>
      </aside>
      
      <main className="main-content">
        {loading ? (
          <div className="loader">Connecting to Backend...</div>
        ) : (
          <>
            {activeTab === 'dashboard' && <Dashboard assets={assets} tickets={tickets} />}
            {activeTab === 'assets' && <Assets assets={assets} />}
            {activeTab === 'tickets' && <Tickets tickets={tickets} />}
          </>
        )}
      </main>
    </div>
  );
}

export default App;
