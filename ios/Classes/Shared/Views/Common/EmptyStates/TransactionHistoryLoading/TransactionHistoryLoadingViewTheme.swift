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

//
//   TransactionHistoryLoadingViewTheme.swift

import Foundation
import MacaroonUIKit

protocol TransactionHistoryLoadingViewTheme:
    StyleSheet,
    LayoutSheet {

    var titleViewCorner: LayoutMetric { get }
    var titleViewSize: LayoutSize { get }
    var titleMargin: LayoutMargins { get }

    var sectionCorner: LayoutMetric { get }
    var sectionSize: LayoutSize { get }
    var sectionMargin: LayoutMargins { get }

    var itemCorner: LayoutMetric { get }
    var itemSize: LayoutSize { get }
    var itemMargin: LayoutMargins { get }
}
