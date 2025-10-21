package com.portfolio.service;
import com.portfolio.entity.Certification;
import com.portfolio.repository.CertificationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import java.util.Optional;

@Service
@Slf4j
public class CertificationService {

    private final CertificationRepository repo;

    public CertificationService(CertificationRepository repo) {
        this.repo = repo;
    }

    @Cacheable(value = "certifications")
    public List<Certification> getAllCertifications() {
        log.info("Fetching all certifications from DB");
        return repo.findAll();
    }


    @Cacheable(value = "certifications", key = "#id")
    public Optional<Certification> findById(Long id) {
        log.info("Fetching certification {} from DB", id);
        return repo.findById(id);
    }


    @CacheEvict(value = "certifications", allEntries = true)
    public Certification save(Certification cert) {
        cert.setVerified(isCertificateValid(cert.getCertificateUrl()));
        return repo.save(cert);
    }

    @CacheEvict(value = "certifications", allEntries = true)
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Async
    @Transactional
    @CacheEvict(value = "certifications", allEntries = true)
    public void verifyAllCertificates() {
        List<Certification> certs = repo.findAll();
        for (Certification cert : certs) {
            boolean isValid = isCertificateValid(cert.getCertificateUrl());
            cert.setVerified(isValid);
            log.info("Verified certificate [{}]: {}", cert.getName(), isValid ? "VALID" : "INVALID");
        }
        repo.saveAll(certs);
    }


    private boolean isCertificateValid(String url) {
        if (url == null || url.isBlank()) return false;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int code = connection.getResponseCode();
            return code >= 200 && code < 400;
        } catch (Exception e) {
            log.warn("Invalid certificate URL: {}", url);
            return false;
        }
    }
}



