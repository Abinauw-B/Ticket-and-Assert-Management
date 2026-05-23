import React from 'react';

const Tickets = ({ tickets }) => {
  return (
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
};

export default Tickets;
