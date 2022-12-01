// Copyright 2022 Pera Wallet, LDA

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//    http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//   SwapCompletedEvent.swift

import Foundation
import MacaroonVendors

struct SwapCompletedEvent: ALGAnalyticsEvent {
    let name: ALGAnalyticsEventName
    let metadata: ALGAnalyticsMetadata

    fileprivate init(mapper: SwapStatusEventMapper) {
        self.name = .swapCompleted

        var mapper = mapper
        guard let params = mapper.mapEventParams() else {
            self.metadata = [:]
            return
        }

        self.metadata = [
            .inputASAID: params.inputASAID,
            .inputASAName: params.inputASAName,
            .inputAmountAsASA: params.inputAmountAsASA,
            .inputAmountAsUSD: params.inputAmountAsUSD,
            .inputAmountAsAlgo: params.inputAmountAsAlgo,
            .outputASAID: params.outputASAID,
            .outputASAName: params.outputASAName,
            .outputAmountAsASA: params.outputAmountAsASA,
            .outputAmountAsUSD: params.outputAmountAsUSD,
            .outputAmountAsAlgo: params.outputAmountAsAlgo,
            .swapDate: params.swapDate,
            .swapDateTimestamp: params.swapDateTimestamp,
            .peraFeeAsAlgo: params.peraFeeAsAlgo,
            .peraFeeAsUSD: params.peraFeeAsUSD,
            .exchangeFeeAsAlgo: params.exchangeFeeAsAlgo,
            .networkFeeAsAlgo: params.networkFeeAsAlgo,
            .swapAddress: params.swapperAddress
        ]
    }
}

extension AnalyticsEvent where Self == SwapCompletedEvent {
    static func swapCompleted(
        quote: SwapQuote,
        parsedTransactions: [ParsedSwapTransaction],
        currency: CurrencyProvider
    ) -> Self {
        return SwapCompletedEvent(
            mapper: SwapStatusEventMapper(
                quote: quote,
                parsedTransactions: parsedTransactions,
                currency: currency
            )
        )
    }
}
