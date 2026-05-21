package com.management.ticketasset.service;

import com.management.ticketasset.model.Asset;
import com.management.ticketasset.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public Asset createAsset(Asset asset) {
        if (assetRepository.findBySerialNumber(asset.getSerialNumber()).isPresent()) {
            throw new IllegalArgumentException("Asset with serial number " + asset.getSerialNumber() + " already exists.");
        }
        return assetRepository.save(asset);
    }

    public Asset updateAsset(Long id, Asset assetDetails) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found with id: " + id));

        asset.setName(assetDetails.getName());
        asset.setDescription(assetDetails.getDescription());
        asset.setStatus(assetDetails.getStatus());
        asset.setAssignedTo(assetDetails.getAssignedTo());
        
        // Only update serial number if it's unique
        if (!asset.getSerialNumber().equals(assetDetails.getSerialNumber())) {
            if (assetRepository.findBySerialNumber(assetDetails.getSerialNumber()).isPresent()) {
                throw new IllegalArgumentException("Asset with serial number " + assetDetails.getSerialNumber() + " already exists.");
            }
            asset.setSerialNumber(assetDetails.getSerialNumber());
        }

        return assetRepository.save(asset);
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found with id: " + id));
        assetRepository.delete(asset);
    }
}
