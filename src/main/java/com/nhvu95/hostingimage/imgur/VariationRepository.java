package com.nhvu95.hostingimage.imgur;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhvu95.hostingimage.image.Image;

@Repository
public interface VariationRepository extends JpaRepository<Variation, UUID> {

}
