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
//   WCSessionListView.swift

import UIKit
import MacaroonUIKit

final class WCSessionListView: View {
    private lazy var theme = WCSessionListViewTheme()

    private(set) lazy var collectionView: UICollectionView = {
        let flowLayout = UICollectionViewFlowLayout()
        flowLayout.minimumLineSpacing = theme.cellSpacing
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: flowLayout)
        collectionView.showsVerticalScrollIndicator = false
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.backgroundColor = theme.backgroundColor.uiColor
        collectionView.contentInset = UIEdgeInsets(theme.contentInset)
        collectionView.register(WCSessionItemCell.self)
        return collectionView
    }()

    override init(frame: CGRect) {
        super.init(frame: frame)

        customize()
    }

    private func customize() {
        addCollectionView()
    }

    func customizeAppearance(_ styleSheet: StyleSheet) { }

    func prepareLayout(_ layoutSheet: LayoutSheet) { }
}

extension WCSessionListView {
    private func addCollectionView() {
        addSubview(collectionView)
        collectionView.pinToSuperview()

        collectionView.backgroundView = ContentStateView()
    }
}

extension WCSessionListView {
    func setDelegate(_ delegate: UICollectionViewDelegate?) {
        collectionView.delegate = delegate
    }

    func setDataSource(_ dataSource: UICollectionViewDataSource?) {
        collectionView.dataSource = dataSource
    }
}
