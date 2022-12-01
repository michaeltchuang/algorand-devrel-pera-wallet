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

//   SwapSummaryPriceImpactItemViewModel.swift

import Foundation
import MacaroonUIKit
import UIKit

struct SwapSummaryPriceImpactItemViewModel: SwapSummaryItemViewModel {
    private(set) var title: TextProvider?
    private(set) var value: TextProvider?

    init(
        _ quote: SwapQuote
    ) {
        bindTitle()
        bindValue(quote)
    }
}

extension SwapSummaryPriceImpactItemViewModel {
    mutating func bindTitle() {
        title = "swap-price-impact-title"
            .localized
            .bodyRegular(lineBreakMode: .byTruncatingTail)
    }

    mutating func bindValue(
        _ quote: SwapQuote
    ) {
        guard let priceImpact = quote.priceImpact else { return }

        value = priceImpact
            .doubleValue
            .toPercentageWith(fractions: 3)?
            .bodyRegular(lineBreakMode: .byTruncatingTail)
    }
}
