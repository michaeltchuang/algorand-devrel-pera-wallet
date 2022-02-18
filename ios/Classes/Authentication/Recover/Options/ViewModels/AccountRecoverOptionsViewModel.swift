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
//   AccountRecoverOptionsViewModel.swift

import UIKit
import MacaroonUIKit

final class AccountRecoverOptionsViewModel: PairedViewModel {
    private(set) var image: UIImage?
    private(set) var title: String?

    init(_ model: AccountRecoverOptionsViewController.Option) {
        bindImage(model)
        bindTitle(model)
    }
}

extension AccountRecoverOptionsViewModel {
    private func bindImage(_ option: AccountRecoverOptionsViewController.Option) {
        switch option {
        case .paste:
            image = img("icon-paste")
        case .scanQR:
            image = img("icon-qr-scan")
        case .info:
            image = img("icon-info-black-24")
        }
    }

    private func bindTitle(_ option: AccountRecoverOptionsViewController.Option) {
        switch option {
        case .paste:
            title = "title-paste-passphrase".localized
        case .scanQR:
            title = "qr-scan-title".localized
        case .info:
            title = "title-learn-more".localized
        }
    }
}
