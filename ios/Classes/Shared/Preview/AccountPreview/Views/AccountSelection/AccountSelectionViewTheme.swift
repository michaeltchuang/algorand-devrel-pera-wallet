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
//   AccountSelectionViewTheme.swift

import Foundation
import MacaroonUIKit
import UIKit

struct AccountSelectionViewTheme: StyleSheet, LayoutSheet {
    let title: TextStyle
    let secondaryTitle: TextStyle
    let checkmarkImage: ImageStyle

    let horizontalInset: LayoutMetric
    let verticalInset: LayoutMetric
    let iconImageSize: LayoutSize
    let checkmarkImageSize: LayoutSize
    
    init(_ family: LayoutFamily) {
        self.title = [
            .textAlignment(.left),
            .textOverflow(SingleLineFittingText()),
            .font(Fonts.DMSans.regular.make(15)),
            .textColor(AppColors.Components.Text.main)
        ]
        self.secondaryTitle = [
            .textAlignment(.left),
            .textOverflow(SingleLineFittingText()),
            .textColor(AppColors.Components.Text.grayLighter),
            .font(Fonts.DMSans.regular.make(13))
        ]
        self.checkmarkImage = [
           .image("icon-circle-check")
       ]
        self.verticalInset = 16
        self.horizontalInset = 16
        self.checkmarkImageSize = (40, 40)
        self.iconImageSize = (40, 40)
    }
}
