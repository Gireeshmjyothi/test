List<MerchantOrderPaymentDto> bookedList = merchantOrderPaymentList.stream()
                    .filter(mop -> TRANSACTION_STATUS_BOOKED.equals(mop.getTransactionStatus()))
                    .toList();

            List<String> atrnNumbers = bookedList.stream().map(MerchantOrderPaymentDto::getAtrnNumber).toList();
