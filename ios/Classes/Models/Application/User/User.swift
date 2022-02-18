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
//  User.swift

import UIKit

final class User: Codable {
    private(set) var accounts: [AccountInformation] = []
    private(set) var defaultNode: String?
    private(set) var deviceId: String?
    
    init(accounts: [AccountInformation]) {
        self.accounts = accounts
    }
    
    func encoded() -> Data? {
        return try? JSONEncoder().encode(self)
    }
}

extension User {
    func addAccount(_ account: AccountInformation) {
        accounts.append(account)
        syncronize()
    }
    
    func removeAccount(_ account: AccountInformation) {
        guard let index = index(of: account) else {
            return
        }
        
        accounts.remove(at: index)
        syncronize()
    }
    
    func index(of account: AccountInformation) -> Int? {
        guard let index = accounts.firstIndex(of: account) else {
            return nil
        }
        
        return index
    }
    
    func account(at index: Int) -> AccountInformation? {
        guard index < accounts.count else {
            return nil
        }
        
        return accounts[index]
    }
    
    func updateAccount(_ account: AccountInformation) {
        guard let index = index(of: account) else {
            return
        }
        
        accounts[index].updateName(account.name)
        accounts[index].type = account.type
        accounts[index].ledgerDetail = account.ledgerDetail
        accounts[index].receivesNotification = account.receivesNotification
        accounts[index].rekeyDetail = account.rekeyDetail
        accounts[index].preferredOrder = account.preferredOrder
        accounts[index].accountImage = account.accountImage
        syncronize()
    }

    func updateLocalAccount(_ updatedAccount: Account) {
        guard let localAccountIndex = indexOfAccount(updatedAccount.address) else {
            return
        }

        accounts[localAccountIndex].updateName(updatedAccount.name ?? "")
        accounts[localAccountIndex].type = updatedAccount.type
        accounts[localAccountIndex].ledgerDetail = updatedAccount.ledgerDetail
        accounts[localAccountIndex].receivesNotification = updatedAccount.receivesNotification
        accounts[localAccountIndex].rekeyDetail = updatedAccount.rekeyDetail
        accounts[localAccountIndex].preferredOrder = updatedAccount.preferredOrder
        accounts[localAccountIndex].accountImage = updatedAccount.accountImage
        syncronize()
    }

    func syncronize() {
        guard UIApplication.shared.appConfiguration?.session.authenticatedUser != nil else {
            return
        }
        
        UIApplication.shared.appConfiguration?.session.authenticatedUser = self
    }
    
    func setDefaultNode(_ node: AlgorandNode?) {
        defer {
            syncronize()
        }
        
        guard let selectedNode = node else {
            self.defaultNode = nil
            return
        }
        
        self.defaultNode = selectedNode.network.rawValue
    }
    
    func preferredAlgorandNetwork() -> ALGAPI.Network? {
        guard let defaultNode = defaultNode else {
            return nil
        }
        
        if defaultNode == ALGAPI.Network.mainnet.rawValue {
            return .mainnet
        } else if defaultNode == ALGAPI.Network.testnet.rawValue {
            return .testnet
        } else {
            return nil
        }
    }
    
    func setDeviceId(_ id: String?) {
        deviceId = id
        NotificationCenter.default.post(name: .DeviceIDDidSet, object: nil)
        syncronize()
    }
    
    func account(address: String) -> AccountInformation? {
        return accountFrom(address: address)
    }

    func indexOfAccount(_ address: String) -> Int? {
        return accounts.firstIndex(where: { $0.address == address })
    }
}

extension User {
    private func accountFrom(address: String) -> AccountInformation? {
        return accounts.first { $0.address == address }
    }
}
