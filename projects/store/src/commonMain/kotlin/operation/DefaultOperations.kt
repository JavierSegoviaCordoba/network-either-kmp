package com.javiersc.either.store.operation

import com.javiersc.either.store.StoreAction.Get
import com.javiersc.either.store.StoreAction.Insert
import com.javiersc.either.store.StoreSource.Cache
import com.javiersc.either.store.StoreSource.Remote
import com.javiersc.either.store.StoreSource.SourceOfTruth

public val operationsCacheSourceOfTruthRemote: List<OperationFlow> =
    operations {
        add { (Get from Cache).emittingLoading() }
        addAndInvoke { (Get from SourceOfTruth).emittingLoading() shouldDo (Insert into Cache) }
        addAndInvoke {
            (Get from Remote).emitting() shouldDo
                listOf(Insert into Cache, Insert into SourceOfTruth)
        }
    }

public val operationsCacheSourceOfTruthRemoteStopping: List<OperationFlow> =
    operations {
        addStopping { (Get from Cache).emittingLoading() }
        addAndInvokeStopping {
            (Get from SourceOfTruth).emittingLoading() shouldDo (Insert into Cache)
        }
        addAndInvokeStopping {
            (Get from Remote).emitting() shouldDo
                listOf(Insert into Cache, Insert into SourceOfTruth)
        }
    }

public val operationsCacheRemote: List<OperationFlow> =
    operations {
        add { (Get from Cache).emittingLoading() }
        addAndInvoke { (Get from Remote).emitting() shouldDo (Insert into Cache) }
    }
