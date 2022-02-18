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
//   AlgoStatisticsViewController+Theme.swift

import MacaroonUIKit
import UIKit

extension AlgoStatisticsViewController {
    struct Theme: LayoutSheet, StyleSheet {
        let backgroundColor: Color
        let dateSelectionModalHeight: LayoutSize
        let modalHeight: LayoutMetric

        init(_ family: LayoutFamily) {
            self.backgroundColor = AppColors.Shared.System.background
            self.dateSelectionModalHeight = (UIScreen.main.bounds.width, 434)
            self.modalHeight = UIScreen.main.bounds.height * 0.8
        }
    }
}
