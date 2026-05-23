import React from 'react';

const Dashboard = ({ assets, tickets }) => {
  return (
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
};

export default Dashboard;
