// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

#import "JavaComponentAccessibility.h"

@interface JavaColumnAccessibility : JavaComponentAccessibility
@end

@interface PlatformAxColumn : PlatformAxElement

@property(readonly) NSUInteger columnNumberInTable;
@end

