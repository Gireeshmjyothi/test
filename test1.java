Map<String, String> errorDetails = new HashMap<>();
            HttpStatusCode status = getWebClient().post().uri(smsBasePath + smsURL).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).header(HttpHeaders.AUTHORIZATION, "Basic " + createBasicAuthHeader(smsUserName, smsPassword)).bodyValue(requestBody).retrieve().onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class).flatMap(errorBody -> {
                logger.error("Error response of SMS notification is {} for smsData {} ", errorBody, smsDto);
                Arrays.stream(errorBody.split("&")).map(pair -> pair.split("=")).forEach(pair -> errorDetails.put(pair[0], pair.length > 1 ? pair[1] : ""));
                return Mono.error(new NotificationException(response.statusCode().toString(), "Error during request: " + errorDetails));
            })).toBodilessEntity()
                    .doOnSuccess(response -> {
                        logger.info("SMS API response status: {}", response.getStatusCode());
                        logger.info("SMS API response.body: {}", response.getBody());
                        logger.info("SMS API full response: {}", response);
                    })
                    .doOnError(error -> {
                        logger.error("Error occurred while calling sms api : {}", error.getMessage());
                        logger.error("Error Object : {0}", error);
                        error.printStackTrace();
                    })
                    .map(ResponseEntity::getStatusCode)
                    .block(); // Blocking for simplicity in this example

            assert status != null;
            if (status.is2xxSuccessful()) {
                logger.debug("Success status of SMS notification is {} for smsData {} ", status, smsDto);
                return true;
            } else {
                logger.error("Error status of SMS notification is {} for smsData {} ", status, smsDto);
                throw new NotificationException(NotificationConstant.FAILURE_CODE, MessageFormat.format(NotificationConstant.FAILURE_MSG, "SMS"));
            }
        } catch (Exception e) {
            logger.error("Error in SMS notification {} for smsData {} ", e.getMessage(), smsDto);
            throw new NotificationException(NotificationConstant.FAILURE_CODE, MessageFormat.format(NotificationConstant.FAILURE_MSG, "SMS"));
        }


curl -k -u 'epaypgotpdom:Ep@y1Dddt' -X POST "https://example.test.co.in:4244/bmg/sms/epaypgotpdom" --data "content_type=text&sender_id=SBIBNK&mobile=8073012970&message=Transaction Success Notification Dear Customer, Transaction of INR 99.9 on NB at Cedge pvt ltd on 23/01/2025 has been successfully processed through SBIePay.&intflag=0&charging=0"
error_code=10008&error_desc=MESSAGE_TEMPLATE_VERIFY_FAILED&umid=112502101548202571914311275
