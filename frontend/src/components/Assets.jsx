import React from 'react';

const Assets = ({ assets }) => {
  return (
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
};

export default Assets;
