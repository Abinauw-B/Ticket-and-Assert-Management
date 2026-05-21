import { useState, useEffect } from 'react';
import { LayoutDashboard, Monitor, Ticket, Users, Activity, HardDrive, Smartphone, Server } from 'lucide-react';

// Using basic auth for the seeded admin user
const API_URL = 'http://localhost:8080/api';
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

  const renderDashboard = () => (
    <div className="fade-in">
      <div className="header">
        <div className="title-section">
          <h1>System Overview</h1>
          <p>Real-time metrics for your IT infrastructure</p>
        </div>
      </div>
      
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-title">Total Assets</div>
          <div className="stat-value">{assets.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-title">Available Assets</div>
          <div className="stat-value">{assets.filter(a => a.status === 'AVAILABLE').length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-title">Open Tickets</div>
          <div className="stat-value">{tickets.filter(t => t.status === 'OPEN').length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-title">Critical Issues</div>
          <div className="stat-value">{tickets.filter(t => t.priority === 'CRITICAL' || t.priority === 'HIGH').length}</div>
        </div>
      </div>

      <div className="header" style={{marginTop: '4rem'}}>
        <div className="title-section">
          <h2>Recent Activity</h2>
          <p>Latest tickets and asset updates</p>
        </div>
      </div>
      
      <div className="data-table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>Ticket ID</th>
              <th>Title</th>
              <th>Priority</th>
              <th>Status</th>
              <th>Created By</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {tickets.slice(0, 5).map(ticket => (
              <tr key={ticket.id}>
                <td>#TCK-{ticket.id.toString().padStart(4, '0')}</td>
                <td>{ticket.title}</td>
                <td>
                  <span className={`badge ${ticket.priority === 'HIGH' ? 'badge-red' : ticket.priority === 'MEDIUM' ? 'badge-yellow' : 'badge-blue'}`}>
                    {ticket.priority}
                  </span>
                </td>
                <td>
                   <span className={`badge ${ticket.status === 'OPEN' ? 'badge-blue' : ticket.status === 'IN_PROGRESS' ? 'badge-yellow' : 'badge-green'}`}>
                    {ticket.status}
                  </span>
                </td>
                <td>{ticket.createdBy?.firstName} {ticket.createdBy?.lastName}</td>
                <td>{new Date(ticket.createdAt).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );

  const renderAssets = () => (
    <div className="fade-in">
      <div className="header">
        <div className="title-section">
          <h1>Asset Directory</h1>
          <p>Manage and track hardware across the organization</p>
        </div>
        <button className="btn btn-primary">Add Asset</button>
      </div>

      <div className="data-table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>Serial Number</th>
              <th>Asset Name</th>
              <th>Category</th>
              <th>Brand / Model</th>
              <th>Status</th>
              <th>Assigned To</th>
              <th>IP Address</th>
              <th>Location</th>
            </tr>
          </thead>
          <tbody>
            {assets.map(asset => (
              <tr key={asset.id}>
                <td style={{fontWeight: 600}}>{asset.serialNumber}</td>
                <td>{asset.name}</td>
                <td>{asset.category}</td>
                <td>{asset.brand} {asset.model}</td>
                <td>
                  <span className={`badge ${asset.status === 'AVAILABLE' ? 'badge-green' : asset.status === 'ASSIGNED' ? 'badge-blue' : asset.status === 'UNDER_REPAIR' ? 'badge-yellow' : 'badge-red'}`}>
                    {asset.status}
                  </span>
                </td>
                <td>{asset.assignedTo ? `${asset.assignedTo.firstName} ${asset.assignedTo.lastName}` : 'Unassigned'}</td>
                <td>{asset.ipAddress || 'N/A'}</td>
                <td>{asset.physicalLocation}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );

  const renderTickets = () => (
    <div className="fade-in">
      <div className="header">
        <div className="title-section">
          <h1>Support Tickets</h1>
          <p>Handle service requests and incidents</p>
        </div>
        <button className="btn btn-primary">New Ticket</button>
      </div>

      <div className="data-table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Category</th>
              <th>Priority</th>
              <th>Status</th>
              <th>Requester</th>
              <th>Assigned Agent</th>
              <th>Created At</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map(ticket => (
              <tr key={ticket.id}>
                <td>#{ticket.id}</td>
                <td style={{fontWeight: 500}}>{ticket.title}</td>
                <td>{ticket.category}</td>
                <td>
                  <span className={`badge ${ticket.priority === 'HIGH' ? 'badge-red' : ticket.priority === 'MEDIUM' ? 'badge-yellow' : 'badge-blue'}`}>
                    {ticket.priority}
                  </span>
                </td>
                <td>
                  <span className={`badge ${ticket.status === 'OPEN' ? 'badge-blue' : ticket.status === 'IN_PROGRESS' ? 'badge-yellow' : 'badge-green'}`}>
                    {ticket.status}
                  </span>
                </td>
                <td>{ticket.createdBy?.firstName} {ticket.createdBy?.lastName}</td>
                <td>{ticket.assignedAgentName || 'Unassigned'}</td>
                <td>{new Date(ticket.createdAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );

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
            {activeTab === 'dashboard' && renderDashboard()}
            {activeTab === 'assets' && renderAssets()}
            {activeTab === 'tickets' && renderTickets()}
          </>
        )}
      </main>
    </div>
  );
}

export default App;
