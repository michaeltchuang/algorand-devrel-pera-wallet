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
//  AssetAlertDraft.swift

import Foundation

struct AssetAlertDraft {
    let account: Account?
    let assetIndex: Int64
    var assetDetail: AssetInformation?
    let title: String?
    let detail: String?
    let actionTitle: String?
    let cancelTitle: String?
    
    init(
        account: Account?,
        assetIndex: Int64,
        assetDetail: AssetInformation?,
        title: String? = nil,
        detail: String? = nil,
        actionTitle: String? = nil,
        cancelTitle: String? = nil
    ) {
        self.account = account
        self.assetIndex = assetIndex
        self.assetDetail = assetDetail
        self.title = title
        self.detail = detail
        self.actionTitle = actionTitle
        self.cancelTitle = cancelTitle
    }
    
    func isValid() -> Bool {
        return assetDetail != nil
    }
}
