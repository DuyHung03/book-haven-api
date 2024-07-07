package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.config.VNPayConfig;
import com.duyhung.bookstoreapi.entity.Payment;
import com.duyhung.bookstoreapi.entity.VNPayResponse;
import com.duyhung.bookstoreapi.repository.PaymentRepository;
import com.duyhung.bookstoreapi.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VNPayService {

    private final VNPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;

    public VNPayResponse createVnPayPayment(HttpServletRequest request) {
        String amountStr = request.getParameter("amount");
        if (amountStr == null || amountStr.isEmpty()) {
            throw new IllegalArgumentException("Amount parameter is missing");
        }

        long amount;
        try {
            amount = Long.parseLong(amountStr) * 100L;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();

        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));

        //bankCode is an option
        String bankCode = request.getParameter("bankCode");
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Build query URL
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.secretKey, hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return VNPayResponse.builder()
                .code("oke")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }

    public void savePayment(HttpServletRequest request) {
        Payment payment = new Payment();
        String responseCode = request.getParameter("vnp_ResponseCode");

        if (responseCode.equals("00"))
            payment.setStatus("Succeed");
        else payment.setStatus("Failed");

        payment.setTxnRef(request.getParameter("vnp_TxnRef"));
        payment.setResponseCode(responseCode);
        payment.setTransactionNo(request.getParameter("vnp_TransactionNo"));
        payment.setBankCode(request.getParameter("vnp_BankCode"));
        payment.setAmount(request.getParameter("vnp_Amount"));
        payment.setOrderInfo(request.getParameter("vnp_OrderInfo"));
        payment.setPayDate(request.getParameter("vnp_PayDate"));
        paymentRepository.save(payment);
    }

}
